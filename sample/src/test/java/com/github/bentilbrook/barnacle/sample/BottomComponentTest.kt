package com.github.bentilbrook.barnacle.sample

import androidx.test.core.app.launchActivity
import com.github.bentilbrook.barnacle.sample.testing.TestActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldContain
import org.junit.Test

@HiltAndroidTest
class BottomComponentTest : BaseTest() {

    private val dispatcher = FakeDispatcher()

    @Test fun `when bottom nav view item selected, should dispatch item selected action`() {
        launchActivity<TestActivity>().onActivity { activity ->
            runBlockingTest {
                val view = BottomNavigationView(activity).apply {
                    inflateMenu(R.menu.bottom_nav)
                }
                BottomComponent(
                    state = flowOf(BottomState()),
                    dispatch = dispatcher,
                    bottomNavigationView = view,
                    scope = this
                )
                val selectedId = R.id.settings
                view.selectedItemId = selectedId
                dispatcher.actions shouldContain NavigationItemSelected(id = selectedId)
            }
        }
    }
}
