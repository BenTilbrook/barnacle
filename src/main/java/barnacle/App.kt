package barnacle

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import barnacle.repolist.RepoList
import barnacle.repolist.RepoListScreen
import barnacle.settings.Settings
import barnacle.settings.SettingsScreen
import barnacle.theme.Theme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun App() {
    Theme {
        val navController = rememberAnimatedNavController()
        Scaffold(
            content = {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "repolist",
                ) {
                    composable(
                        route = RepoListScreen.route,
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { 1000 },
                                animationSpec = tween(700))
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -1000 },
                                animationSpec = tween(700))
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { -1000 },
                                animationSpec = tween(700))
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { 1000 },
                                animationSpec = tween(700))
                        }
                    ) {
                        RepoList()
                    }
                    composable(
                        route = SettingsScreen.route,
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { 1000 },
                                animationSpec = tween(700))
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -1000 },
                                animationSpec = tween(700))
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { -1000 },
                                animationSpec = tween(700))
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { 1000 },
                                animationSpec = tween(700))
                        }
                    ) {
                        Settings()
                    }
                }
            },
            bottomBar = {
                TabBar(navController, screens)
            }
        )
    }
}

private val screens = listOf(
    RepoListScreen,
    SettingsScreen,
)
