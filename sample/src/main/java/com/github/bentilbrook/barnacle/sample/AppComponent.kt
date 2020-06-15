package com.github.bentilbrook.barnacle.sample

import androidx.constraintlayout.widget.ConstraintLayout
import com.github.bentilbrook.barnacle.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun AppComponent(
    state: Flow<AppState>,
    dispatch: Dispatcher,
    parent: ConstraintLayout,
    scope: CoroutineScope
) {
    scope.launch {
        state.collect {

        }
    }
}
