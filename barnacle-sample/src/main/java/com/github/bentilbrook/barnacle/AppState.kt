package com.github.bentilbrook.barnacle

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppState(val isLoading: Boolean = false) : Parcelable
