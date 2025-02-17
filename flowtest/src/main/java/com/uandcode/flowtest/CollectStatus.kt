package com.uandcode.flowtest

/**
 * The current state of collecting Kotlin Flow
 */
public sealed class CollectStatus {

    /**
     * Flow hasn't been finished yet and is being collected
     */
    public data object Collecting : CollectStatus()

    /**
     * Flow has been completed successfully
     */
    public data object Completed : CollectStatus()

    /**
     * Flow collector has been cancelled
     */
    public data object Cancelled : CollectStatus()

    /**
     * Flow collector has been failed with exception
     */
    public data class Failed(val exception: Exception) : CollectStatus()
}
