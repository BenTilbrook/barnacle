package com.github.bentilbrook.barnacle

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class ComposeMiddlewareTest {
    @Test fun `middleware should be invoked in reverse order`() {
        val invoked = mutableListOf<String>()
        val a: Middleware<Any> = {
            { next -> invoked += "a"; next }
        }
        val b: Middleware<Any> = {
            { next -> invoked += "b"; next }
        }
        val m = composeMiddleware(a, b)
        Store(initialState = Any(), reducer = { state, _ -> state }, middleware = m)
        invoked shouldBeEqualTo listOf("b", "a")
    }

    @Test fun `each middleware should receive the previous dispatcher`() {
//        var receivedDispatcher: Dispatcher? = null
//        val a: Middleware<Any> = {
//            { next -> receivedDispatcher = next; next }
//        }
//        val b: Middleware<Any> = {
//            { next -> next }
//        }
//        val m = composeMiddleware(a, b)
//        Store(initialState = Any(), reducer = { state, _ -> state }, middleware = m)
//        receivedDispatcher shouldBe TODO()
    }
}
