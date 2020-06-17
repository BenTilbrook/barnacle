package com.github.bentilbrook.barnacle.sample

import com.github.bentilbrook.barnacle.Action
import com.github.bentilbrook.barnacle.Dispatcher

class FakeDispatcher : Dispatcher {

    private val _actions = mutableListOf<Action>()

    val actions: List<Action> get() = _actions

    override fun invoke(action: Action) {
        _actions += action
    }
}
