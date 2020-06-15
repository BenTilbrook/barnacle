package com.github.bentilbrook.barnacle.sample

import com.github.bentilbrook.barnacle.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

fun rootReducer(state: AppState, action: Action): AppState {
    return state
}

fun rootEpic(actions: Flow<Action>, state: Flow<AppState>): Flow<Action> = merge(
)
