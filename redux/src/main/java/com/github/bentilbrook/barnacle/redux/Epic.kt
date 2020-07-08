package com.github.bentilbrook.barnacle.redux

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.merge

typealias Epic<A, S> = (actions: Flow<S>, state: Flow<A>) -> Flow<S>

fun <A, S> emptyEpic(): Epic<A, S> = { _: Flow<S>, _: Flow<A> -> emptyFlow() }

class EpicMiddleware<S>(private val epics: List<Epic<S, Action>>) : Middleware<S> {

    constructor(vararg epics: Epic<S, Action>) : this(epics.toList())

    private val channel = BroadcastChannel<Action>(100)
    private lateinit var store: Store<S>

    override fun invoke(store: Store<S>): (next: Dispatcher) -> Dispatcher {
        this.store = store
        return { next ->
            { action ->
                next(action)
                channel.offer(action)
            }
        }
    }

    suspend fun run() {
        val inputActions = channel.asFlow()
        val outputActionFlows = epics.map { it(inputActions, store.state()) }
        outputActionFlows.merge().collect { store.dispatch(it) }
    }
}
