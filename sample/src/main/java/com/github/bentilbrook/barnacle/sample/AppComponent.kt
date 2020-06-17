package com.github.bentilbrook.barnacle.sample

import android.os.Parcelable
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.bentilbrook.barnacle.Action
import com.github.bentilbrook.barnacle.Dispatcher
import com.github.bentilbrook.barnacle.core.layoutInflater
import com.github.bentilbrook.barnacle.sample.databinding.AppComponentBinding
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

fun AppComponent(
    state: Flow<AppState>,
    dispatch: Dispatcher,
    parent: ConstraintLayout,
    scope: CoroutineScope
) {
    val binding = AppComponentBinding.inflate(parent.layoutInflater(), parent)
    BottomComponent(
        state = state.map { it.bottomState },
        dispatch = dispatch,
        bottomNavigationView = binding.bottomNavigationView,
        scope = scope
    )
}

@Parcelize
data class AppState(
    val isLoading: Boolean = false,
    val bottomState: BottomState = BottomState()
) : Parcelable

fun appReducer(state: AppState, action: Action): AppState =
    when (action) {
        is BottomAction -> state.copy(bottomState = bottomReducer(state.bottomState, action))
        else -> state
    }

fun appEpic(actions: Flow<Action>, state: Flow<AppState>): Flow<Action> = merge(
)
