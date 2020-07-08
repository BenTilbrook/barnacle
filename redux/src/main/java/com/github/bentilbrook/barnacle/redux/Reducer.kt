package com.github.bentilbrook.barnacle.redux

typealias Reducer<S, A> = (state: S, action: A) -> S
