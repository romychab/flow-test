package com.uandcode.flowtest.impl

import com.uandcode.flowtest.CollectStatus
import com.uandcode.flowtest.TestFlowCollector
import kotlinx.coroutines.Job

internal class TestFlowCollectorImpl<T>(
    private val job: Job,
    private val collectorState: CollectorState<T>,
) : TestFlowCollector<T> {

    override val collectStatus: CollectStatus
        get() = collectorState.collectStatus

    override val collectedItems: List<T>
        get() = collectorState.collectedItems

    override fun cancel() {
        job.cancel()
    }

}

internal class CollectorState<T>(
    val collectedItems: MutableList<T> = mutableListOf(),
    var collectStatus: CollectStatus = CollectStatus.Collecting,
)
