package com.github.bentilbrook.barnacle

typealias Reducer<S, A> = (state: S, action: A) -> S
