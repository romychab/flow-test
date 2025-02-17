@file:OptIn(ExperimentalCoroutinesApi::class)

package com.uandcode.flowtest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlin.coroutines.CoroutineContext

/**
 * This scope provides a helper [startCollecting] method for
 * easier testing of Kotlin Flows.
 */
public interface FlowTestScope {

    /**
     * Origin [TestScope] instance
     */
    public val scope: TestScope

    /**
     * Start collecting [this] flow.
     * @return TestFlowCollector instance which can be used for asserting the
     *         current collecting status
     */
    public fun <T> Flow<T>.startCollecting(
        context: CoroutineContext = UnconfinedTestDispatcher(scope.testScheduler),
    ): TestFlowCollector<T>

    /**
     * @see [TestScope.advanceUntilIdle]
     */
    public fun advanceUntilIdle(): Unit = scope.advanceUntilIdle()

    /**
     * @see [TestScope.advanceTimeBy]
     */
    public fun advanceTimeBy(millis: Long): Unit = scope.advanceTimeBy(millis)

    /**
     * @see [TestScope.runCurrent]
     */
    public fun runCurrent(): Unit = scope.runCurrent()

}
