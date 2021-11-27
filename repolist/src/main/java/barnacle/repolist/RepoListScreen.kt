package barnacle.repolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import barnacle.backend.Repo
import barnacle.core.Screen
import okhttp3.HttpUrl

object RepoListScreen : Screen(route = "repolist", name = "Repo List", icon = Icons.Filled.Home)

@Composable
fun RepoList() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Repos") })
        },
        content = {
            RepoList(
                repos = repos,
                onRepoClick = {}
            )
        }
    )
}

@Composable
private fun RepoList(repos: List<Repo>?, onRepoClick: (String) -> Unit) {
    val contentPadding = 16.dp
    return when {
        repos == null -> {
            Text("Loading...", modifier = Modifier.padding(contentPadding))
        }
        repos.isEmpty() -> {
            Text("No repos here", modifier = Modifier.padding(contentPadding))
        }
        else -> {
            LazyColumn(
                contentPadding = PaddingValues(contentPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(repos) { repo ->
                    RepoItem(
                        repo = repo,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onRepoClick(repo.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RepoItem(
    repo: Repo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = repo.fullName,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    )
}

@Preview @Composable
private fun PreviewRepoListScreen() {
    RepoList(repos = repos, onRepoClick = {})
}

internal data class RepoItem(
    val id: String,
    val owner: String,
    val name: String,
    val text: String,
    val imageUri: HttpUrl?,
)

private fun Repo.toItem() = RepoItem(
    id = id,
    owner = owner.login,
    name = name,
    text = fullName,
    imageUri = owner.imageUri
)
