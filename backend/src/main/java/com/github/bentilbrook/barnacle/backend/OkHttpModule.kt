package com.github.bentilbrook.barnacle.backend

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {
    @Provides @Singleton
    fun client(interceptors: @JvmSuppressWildcards Set<Interceptor>): OkHttpClient =
        OkHttpClient.Builder()
            .apply { interceptors.forEach { addInterceptor(it) } }
            .build()

    @Provides @ElementsIntoSet
    fun defaultInterceptors(): Set<Interceptor> = emptySet()
}
