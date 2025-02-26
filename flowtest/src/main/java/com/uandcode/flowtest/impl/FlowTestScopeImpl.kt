package com.uandcode.flowtest.impl

import com.uandcode.flowtest.BackgroundCoroutineState
import com.uandcode.flowtest.CollectStatus
import com.uandcode.flowtest.FlowTestScope
import com.uandcode.flowtest.JobStatus
import com.uandcode.flowtest.TestFlowCollector
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlin.coroutines.CoroutineContext

internal class FlowTestScopeImpl(
    override val scope: TestScope,
) : FlowTestScope {

    override fun <T> Flow<T>.startCollecting(
        context: CoroutineContext,
    ): TestFlowCollector<T> {
        val collectorState = CollectorState<T>()
        val job = scope.backgroundScope.launch(context) {
            try {
                toList(collectorState.collectedItems)
                collectorState.collectStatus = CollectStatus.Completed
            } catch (e: CancellationException) {
                collectorState.collectStatus = CollectStatus.Cancelled
            } catch (e: Exception) {
                collectorState.collectStatus = CollectStatus.Failed(e)
            }
        }
        return TestFlowCollectorImpl(job, collectorState)
    }

    override fun <T> executeInBackground(
        context: CoroutineContext,
        command: suspend () -> T
    ): BackgroundCoroutineState<T> {
        var status: JobStatus<T> = JobStatus.Executing
        val job = scope.backgroundScope.launch(context) {
            try {
                val result = command()
                status = JobStatus.Completed(result)
            } catch (e: CancellationException) {
                status = JobStatus.Cancelled
            } catch (e: Exception) {
                status = JobStatus.Failed(e)
            }
        }
        return BackgroundCoroutineStateImpl(
            statusGetter = { status },
            job = job,
        )
    }

}
