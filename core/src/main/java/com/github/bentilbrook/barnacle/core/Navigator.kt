package com.github.bentilbrook.barnacle.core

import androidx.compose.ambientOf

class Navigator {
    fun push(key: Key) {
        TODO()
    }

    fun pop() {
        TODO()
    }
}

interface Key

val Nav = ambientOf<Navigator>()
