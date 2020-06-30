package com.github.bentilbrook.barnacle.sample

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@AssistedModule
@Module(includes = [AssistedInject_AssistedInjectModule::class])
@InstallIn(ApplicationComponent::class)
abstract class AssistedInjectModule
