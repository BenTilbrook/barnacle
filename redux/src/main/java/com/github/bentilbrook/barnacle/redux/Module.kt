package com.github.bentilbrook.barnacle.redux

open class Module<T, S, A : Action>(
    val selector: (T) -> S,
    val reducer: Reducer<S, A>,
    val epic: Epic<S, A> = emptyEpic()
)
