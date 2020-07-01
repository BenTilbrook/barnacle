package com.github.bentilbrook.barnacle.sample

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class BaseTest {
    protected val application: Application = ApplicationProvider.getApplicationContext()
}
