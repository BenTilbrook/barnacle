package com.github.bentilbrook.barnacle

import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.material.Scaffold
import com.github.bentilbrook.barnacle.backend.Api
import com.github.bentilbrook.barnacle.core.Nav
import com.github.bentilbrook.barnacle.core.Navigator
import com.github.bentilbrook.barnacle.repolist.RepoListScreen
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class App @Inject constructor(private val api: Api) {
    @Composable
    operator fun invoke() {
        var selectedTab by state { Tab.BROWSE }
        val navigator = Navigator()
        Providers(Nav provides navigator) {
            Scaffold(
                bodyContent = {
                    RepoListScreen(api::search.asFlow().map { it.repos })
                },
                bottomBar = {
                    TabBar(selectedTab) { selectedTab = it }
                }
            )
        }
    }
}

//@Preview
//@Composable
//fun PreviewApp() {
//    App()
//}
