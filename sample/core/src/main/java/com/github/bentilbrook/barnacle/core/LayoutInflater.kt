package com.github.bentilbrook.barnacle.core

import android.view.LayoutInflater
import android.view.View

fun View.layoutInflater(): LayoutInflater = LayoutInflater.from(context)
