package com.github.bentilbrook.barnacle.sample

import android.os.Parcelable
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.bentilbrook.barnacle.Action
import com.github.bentilbrook.barnacle.Dispatcher
import com.github.bentilbrook.barnacle.Epic
import com.github.bentilbrook.barnacle.sample.core.layoutInflater
import com.github.bentilbrook.barnacle.sample.databinding.AppComponentBinding
import com.github.bentilbrook.barnacle.sample.repolist.RepoListEpic
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

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

class AppEpic @Inject constructor(private val repoListEpic: RepoListEpic) : Epic<Action, AppState> {
    override fun invoke(actions: Flow<Action>, state: Flow<AppState>): Flow<Action> {
        return merge(
            repoListEpic(actions.filterIsInstance(), emptyFlow()) // TODO
        )
    }
}
