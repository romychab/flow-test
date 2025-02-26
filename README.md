# Flow Test

A tiny library for easier testing of Kotlin Flows in a sequential way, without direct usage
of `backgroundScope.launch { ... }`.

[![Maven Central](https://img.shields.io/maven-central/v/com.uandcode/flowtest.svg?label=Maven%20Central)](https://uandcode.com/sh/flowtest)
[![License: Apache 2](https://img.shields.io/github/license/romychab/flow-test)](LICENSE)

## Installation

```groovy
// flowtest library:
testImplementation "com.uandcode.flowtest:1.0.0"
// + default jetbrains library for testing coroutines if needed:
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1"
```

## Usage example

```kotlin
@Test
fun testFlow() = runFlowTest { // <-- write runFlowTest instead of runTest
    val repository: UserRepository = createRepository()
    val currentUserFlow: Flow<User> = repository.getCurrentUser()
    val testFlowCollector: TestFlowCollector<User> = currentUserFlow.startCollecting()

    // assert the latest collected item
    assertEquals(User.ANONYMOUS, testFlowCollector.lastItem)
    // do something to make the flow produce a new value        
    repository.signIn("email", "password")
    // assert the latest collected item again
    assertEquals(User("email", "password"), testFlowCollector.lastItem)

    // example of other assertions:
    assertEquals(2, testFlowCollector.count)
    assertTrue(testFlowCollector.hasItems)
    assertEquals(
        listOf(User.ANONYMOUS, User("email", "password")),
        testFlowCollector.collectedItems,
    )
    assertEquals(CollectStatus.Collecting, testFlowCollector.collectStatus)
}
```

## Documentation

Check out [this link](https://docs.uandcode.com/flowtest/) for more details.
