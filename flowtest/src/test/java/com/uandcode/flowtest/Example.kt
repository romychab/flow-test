package com.uandcode.flowtest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleTest {
    @Test
    fun test() = runFlowTest {
        val repository = Repository()

        val collector = repository.getCurrentAccount().startCollecting()
        assertEquals(User.Anonymous, collector.lastItem)

        repository.signIn("admin@google.com", "123")
        assertEquals(User(1, "Google Admin"), collector.lastItem)

        repository.logout()
        assertEquals(User.Anonymous, collector.lastItem)
    }
}

internal data class User(
    val id: Long,
    val name: String,
) {
    companion object {
        val Anonymous = User(0, "Anonymous")
    }
}

internal class Repository {

    private val userFlow = MutableStateFlow(User.Anonymous)

    fun getCurrentAccount(): Flow<User> {
        return userFlow
    }

    suspend fun signIn(email: String, password: String) {
        delay(1000)
        if (email == "admin@google.com" && password == "123") {
            userFlow.value = User(id = 1, name = "Google Admin")
        } else {
            throw IllegalArgumentException("Invalid email or password")
        }
    }

    suspend fun logout() {
        delay(1000)
        userFlow.value = User.Anonymous
    }

}
