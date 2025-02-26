package com.uandcode.flowtest.impl

import com.uandcode.flowtest.BackgroundCoroutineState
import com.uandcode.flowtest.JobStatus
import kotlinx.coroutines.Job

internal class BackgroundCoroutineStateImpl<T>(
    private val statusGetter: () -> JobStatus<T>,
    private val job: Job,
) : BackgroundCoroutineState<T> {

    override val status: JobStatus<T> get() = statusGetter()

    override fun cancel() {
        job.cancel()
    }

}
