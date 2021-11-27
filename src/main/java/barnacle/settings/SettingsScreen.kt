package barnacle.settings

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import barnacle.core.Screen

object SettingsScreen : Screen(route = "settings", name = "Settings", icon = Icons.Filled.Settings)

@Composable
fun Settings() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        content = {
            Text(text = "Settings screen")
        }
    )
}
