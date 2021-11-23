package com.github.bentilbrook.barnacle

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable

@Composable
fun TabBar(selectedTab: Tab, onTabClick: (Tab) -> Unit) {
    BottomNavigation {
        enumValues<Tab>().map { tab ->
            BottomNavigationItem(
                icon = { Icon(tab.icon, contentDescription = null) },
                selected = selectedTab == tab,
                onClick = { onTabClick(tab) }
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
