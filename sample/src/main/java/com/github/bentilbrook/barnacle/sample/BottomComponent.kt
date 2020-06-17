package com.github.bentilbrook.barnacle.sample

import android.os.Parcelable
import com.github.bentilbrook.barnacle.Action
import com.github.bentilbrook.barnacle.Dispatcher
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun BottomComponent(
    state: Flow<BottomState>,
    dispatch: Dispatcher,
    bottomNavigationView: BottomNavigationView,
    scope: CoroutineScope
) {
    bottomNavigationView.setOnNavigationItemSelectedListener {
        dispatch(BottomNavigationItemSelectedAction(it.itemId)); true
    }
    state.map { it.selectedItemId }
        .distinctUntilChanged()
        .onEach { bottomNavigationView.selectedItemId = it }
        .launchIn(scope)
}

@Parcelize
data class BottomState(val selectedItemId: Int = R.id.settings) : Parcelable

data class BottomNavigationItemSelectedAction(val id: Int) : Action