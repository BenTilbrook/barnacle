package com.github.bentilbrook.barnacle.repolist

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview
import com.github.bentilbrook.barnacle.backend.Repo
import com.github.bentilbrook.barnacle.backend.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.HttpUrl

@Composable
fun RepoListScreen(repos: Flow<List<Repo>>) = Scaffold(
    topBar = {
        TopAppBar(title = { Text("Repos") })
    },
    bodyContent = {
        RepoList(repos.collectAsState(initial = null).value)
    }
)

@Composable
private fun RepoList(repos: List<Repo>?) = when {
    repos == null -> {
        Text("Loading...")
    }
    repos.isEmpty() -> {
        Text("No repos here")
    }
    else -> {
        LazyColumnItems(items = repos) { repo -> Text(repo.fullName) }
    }
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
