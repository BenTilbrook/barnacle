package com.github.bentilbrook.barnacle

import androidx.compose.Composable
import androidx.ui.foundation.Icon
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Home
import androidx.ui.material.icons.filled.Settings

@Composable
fun TabBar(selectedTab: Tab, onSelected: (Tab) -> Unit) {
    BottomNavigation {
        enumValues<Tab>().map { tab ->
            BottomNavigationItem(
                icon = { Icon(tab.icon) },
                selected = selectedTab == tab,
                onSelected = { onSelected(tab) }
            )
        }
    }
}

enum class Tab { BROWSE, SETTINGS }

private val Tab.icon
    get() = when (this) {
        Tab.BROWSE -> Icons.Filled.Home
        Tab.SETTINGS -> Icons.Filled.Settings
    }
