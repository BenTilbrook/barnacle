package com.github.bentilbrook.barnacle.backend

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.time.Instant
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule {
    @Provides @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .add(HttpUrlJsonAdapter)
        .add(InstantAdapter)
        .build()
}

private object HttpUrlJsonAdapter {
    @FromJson fun stringToHttpUrl(string: String): HttpUrl = string.toHttpUrl()
    @ToJson fun httpUrlToString(HttpUrl: HttpUrl): String = HttpUrl.toString()
}

private object InstantAdapter {
    @FromJson fun stringToInstant(string: String): Instant = Instant.parse(string)
    @ToJson fun instantToString(instant: Instant): String = instant.toString()
}
