package com.github.bentilbrook.barnacle

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge

typealias Epic<A, S> = (actions: Flow<A>, state: Flow<S>) -> Flow<A>

class EpicMiddleware<S>(private val epics: List<Epic<Action, S>>) : Middleware<S> {

    constructor(vararg epics: Epic<Action, S>) : this(epics.toList())

    private val channel = BroadcastChannel<Action>(Channel.UNLIMITED)
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
        outputActionFlows.merge().collect { channel.offer(it) }
    }
}
