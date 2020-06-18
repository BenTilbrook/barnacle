package com.github.bentilbrook.barnacle.sample

import dagger.hilt.android.testing.HiltAndroidTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

@HiltAndroidTest
class BottomReducerTest {
    @Test fun `when nav item selected dispatched, should select the item`() {
        bottomReducer(
            BottomState(selectedItemId = R.id.settings),
            NavigationItemSelected(R.id.browse)
        ).selectedItemId shouldBeEqualTo R.id.browse
    }
}
