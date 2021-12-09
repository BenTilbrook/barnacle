package barnacle

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import barnacle.repolist.ReposTab
import barnacle.repolist.reposGraph
import barnacle.settings.SettingsTab
import barnacle.settings.settingsGraph
import barnacle.theme.Theme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun App() {
    Theme {
        val navController = rememberAnimatedNavController()
        Scaffold(
            content = {
                AnimatedNavHost(navController = navController, startDestination = ReposTab.route) {
                    reposGraph(navController)
                    settingsGraph(navController)
                }
            },
            bottomBar = {
                TabBar(navController, tabs)
            }
        )
    }
}

private val tabs = listOf(ReposTab, SettingsTab)
