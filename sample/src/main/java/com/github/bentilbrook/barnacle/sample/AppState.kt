package com.github.bentilbrook.barnacle.sample

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppState(val isLoading: Boolean = false) : Parcelable
