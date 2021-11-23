package com.github.bentilbrook.barnacle

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.bentilbrook.barnacle.backend.Api
import com.github.bentilbrook.barnacle.repolist.RepoListScreen
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class App @Inject constructor(private val api: Api) {
    @Composable
    operator fun invoke() {
        Theme {
            var selectedTab by remember { mutableStateOf(Tab.BROWSE) }
            Scaffold(
                content = {
                    RepoListScreen(api::search.asFlow().map { it.repos })
                },
                bottomBar = {
                    TabBar(selectedTab) { selectedTab = it }
                }
            )
        }

    }
}
