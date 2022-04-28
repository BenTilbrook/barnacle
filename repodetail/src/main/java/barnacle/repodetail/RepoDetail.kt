package barnacle.repodetail

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import barnacle.core.Screen

object RepoDetailScreen : Screen(route = "repo/{id}", name = "Repo Detail") {
    fun route(id: String) = "repo/$id"
}

@Composable
fun RepoDetail(id: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Repo $id") })
        },
        content = {
            Text(text = "Repo $id")
        },
    )
}

@Preview @Composable
private fun PreviewRepoDetail() {
    RepoDetail(id = "1")
}
