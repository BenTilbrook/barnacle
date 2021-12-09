package barnacle.settings

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import barnacle.core.Screen
import barnacle.core.Tab
import com.google.accompanist.navigation.animation.composable

object SettingsTab : Tab(route = "settings", name = "Settings", icon = Icons.Filled.Settings)

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(route = SettingsTab.route, startDestination = AllSettingsScreen.route) {
        composable(AllSettingsScreen.route) {
            AllSettings()
        }
    }
}

private object AllSettingsScreen : Screen(route = "allSettings", name = "Settings")

@Composable
private fun AllSettings() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        content = {
            Text(text = "Settings")
        },
    )
}
