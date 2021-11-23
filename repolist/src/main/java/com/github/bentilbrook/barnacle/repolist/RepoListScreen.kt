package com.github.bentilbrook.barnacle.repolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.bentilbrook.barnacle.backend.Repo
import com.github.bentilbrook.barnacle.backend.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.HttpUrl

@Composable
fun RepoListScreen(repos: Flow<List<Repo>>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Repos") })
        },
        content = {
            RepoList(
                repos = repos.collectAsState(initial = null).value,
                onRepoClick = { TODO() }
            )
        }
    )
}

@Composable
private fun RepoList(repos: List<Repo>?, onRepoClick: (String) -> Unit) = when {
    repos == null -> {
        Text("Loading...")
    }
    repos.isEmpty() -> {
        Text("No repos here")
    }
    else -> {
        LazyColumn {
            items(repos) { repo ->
                RepoItem(
                    repo = repo,
                    onClick = { onRepoClick(repo.id) }
                )
            }
        }
    }
}

@Composable
private fun RepoItem(repo: Repo, onClick: () -> Unit) {
    Text(text = repo.fullName, modifier = Modifier.clickable(onClick = onClick))
}

@Preview @Composable
private fun PreviewRepoListScreen() {
    RepoListScreen(
        repos = flowOf(
            (0..100).map {
                Repo(
                    id = "1",
                    name = "Repo 1",
                    fullName = "Full Repo 1",
                    description = "This is a repo",
                    starCount = 1,
                    owner = User(
                        id = "1",
                        login = "user1",
                        name = "User",
                        location = "Location",
                        bio = "Bio",
                        followerCount = 1,
                        followingCount = 2,
                        imageUri = null
                    )
                )
            }
        )
    )
}

internal data class RepoItem(
    val id: String,
    val owner: String,
    val name: String,
    val text: String,
    val imageUri: HttpUrl?
)

private fun Repo.toItem() = RepoItem(
    id = id,
    owner = owner.login,
    name = name,
    text = fullName,
    imageUri = owner.imageUri
)
