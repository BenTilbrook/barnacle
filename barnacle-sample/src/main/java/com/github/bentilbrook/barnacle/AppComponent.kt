package com.github.bentilbrook.barnacle

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Suppress("RemoveRedundantQualifierName")
@Singleton
@Component(
    modules = [
        dagger.android.AndroidInjectionModule::class
    ]
)
internal interface AppComponent : AndroidInjector<BarnacleApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    companion object {
        internal fun create(context: Context): AppComponent = DaggerAppComponent
            .factory()
            .create(context.applicationContext as Application)
    }
}
