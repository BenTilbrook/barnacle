package com.github.bentilbrook.barnacle.redux

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Store<S>(
    initialState: S,
    private val reducer: Reducer<S, Action>,
    middleware: Middleware<S> = emptyMiddleware()
) {
    private val state = MutableStateFlow(initialState)
    private val dispatcher = middleware(this)(::dispatchCore)

    fun state(): StateFlow<S> = state

    fun dispatch(action: Action) = dispatcher(action)

    private fun dispatchCore(action: Action) {
        state.value = reducer(state.value, action)
    }
}
