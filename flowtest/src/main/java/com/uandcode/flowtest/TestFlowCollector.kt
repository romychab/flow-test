package com.uandcode.flowtest

public interface TestFlowCollector<T> {

    /**
     * Cancel collecting the flow.
     */
    public fun cancel()

    /**
     * Get the list of all collected items emitted by the flow.
     */
    public val collectedItems: List<T>

    /**
     * Get the current status of collecting items
     * @see CollectStatus
     */
    public val collectStatus: CollectStatus

    /**
     * Total count of collected items
     */
    public val count: Int get() = collectedItems.size

    /**
     * Whether there is at least one collected item
     */
    public val hasItems: Boolean get() = count > 0

    /**
     * The last collected item from the flow
     */
    public val lastItem: T get() = collectedItems.last()

}
