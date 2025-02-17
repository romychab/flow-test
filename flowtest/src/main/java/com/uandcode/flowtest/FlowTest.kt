@file:OptIn(ExperimentalCoroutinesApi::class)

package com.uandcode.flowtest

import com.uandcode.flowtest.impl.FlowTestScopeImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * Start a unit test for testing suspend functions and Kotlin Flows
 * using existing TestScope.
 */
public fun TestScope.runFlowTest(
    block: suspend FlowTestScope.() -> Unit,
) {
    runTest {
        val flowTestScope = FlowTestScopeImpl(this)
        flowTestScope.block()
    }
}

/**
 * Create a new TestScope and start a unit test for testing Kotlin Flow / suspend
 * functions.
 */
public fun runFlowTest(
    block: suspend FlowTestScope.() -> Unit,
) {
    TestScope().runFlowTest(block)
}
