package com.github.bentilbrook.barnacle

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BarnacleApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<BarnacleApplication> =
        AppComponent.create(this)
}
