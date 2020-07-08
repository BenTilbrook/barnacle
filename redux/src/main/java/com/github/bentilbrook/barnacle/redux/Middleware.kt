package com.github.bentilbrook.barnacle.redux

import androidx.annotation.CheckResult

typealias Middleware<S> = (store: Store<S>) -> (next: Dispatcher) -> Dispatcher

fun <S> middleware(dispatch: (Store<S>, next: Dispatcher, action: Action) -> Unit): Middleware<S> =
    { store ->
        { next ->
            { action ->
                dispatch(store, next, action)
            }
        }
    }

fun <S> emptyMiddleware(): Middleware<S> = middleware { _, next, action -> next(action) }

fun <S> composeMiddleware(middlewares: List<Middleware<S>>): Middleware<S> = { store ->
    // Build list of dispatch creators
    val chain = middlewares.map { it(store) };
    { next ->
        // Return a dispatcher where the above are invoked right-to-left
        chain.foldRight(next) { dispatcherCreator, nextDispatcher ->
            dispatcherCreator(nextDispatcher)
        }
    }
}

@CheckResult fun <S> composeMiddleware(vararg middlewares: Middleware<S>): Middleware<S> =
    composeMiddleware(middlewares.asList())
