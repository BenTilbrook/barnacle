package com.github.bentilbrook.barnacle

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Store<S>(
    initialState: S,
    private val reduce: (S, Action) -> S
) {
    private val state = MutableStateFlow(initialState)

    fun state(): StateFlow<S> = state

    fun dispatch(action: Action) {
        state.value = reduce(state.value, action)
    }
}
