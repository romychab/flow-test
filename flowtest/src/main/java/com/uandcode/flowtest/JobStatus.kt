package com.uandcode.flowtest

public sealed class JobStatus<out T> {
    public data object Executing : JobStatus<Nothing>()
    public data object Cancelled : JobStatus<Nothing>()
    public data class Completed<out T>(val result: T) : JobStatus<T>()
    public data class Failed(val exception: Exception) : JobStatus<Nothing>()
}
