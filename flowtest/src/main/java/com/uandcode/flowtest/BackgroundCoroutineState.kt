package com.uandcode.flowtest

/**
 * This interface represents the current state of a command execution
 * started by [FlowTestScope.executeInBackground].
 */
public interface BackgroundCoroutineState<T> {

    /**
     * Get the current status of a command execution.
     */
    public val status: JobStatus<T>

    /**
     * Cancel the execution of a command.
     */
    public fun cancel()

}
