package barnacle.repolist

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import barnacle.backend.Repo
import barnacle.core.Screen
import barnacle.core.Tab
import barnacle.repodetail.RepoDetail
import barnacle.repodetail.RepoDetailScreen
import com.google.accompanist.navigation.animation.composable
import okhttp3.HttpUrl

object ReposTab : Tab(
    route = "repos",
    name = "Repos",
    icon = Icons.Filled.Home,
)

fun NavGraphBuilder.reposGraph(navController: NavController) {
    navigation(
        route = ReposTab.route,
        startDestination = RepoListScreen.route,
    ) {
        composable(
            route = RepoListScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = HalfWidthOffsetPositive,
                    animationSpec = AnimationSpec,
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = HalfWidthOffsetNegative,
                    animationSpec = AnimationSpec,
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = HalfWidthOffsetNegative,
                    animationSpec = AnimationSpec,
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = HalfWidthOffsetPositive,
                    animationSpec = AnimationSpec,
                )
            },
        ) {
            RepoList(onRepoClick = { id -> navController.navigate(RepoDetailScreen(id)) })
        }
        composable(
            route = RepoDetailScreen.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = HalfWidthOffsetPositive,
                    animationSpec = AnimationSpec,
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = HalfWidthOffsetNegative,
                    animationSpec = AnimationSpec,
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = HalfWidthOffsetNegative,
                    animationSpec = AnimationSpec,
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = HalfWidthOffsetPositive,
                    animationSpec = AnimationSpec,
                )
            },
        ) {
            RepoDetail(id = it.arguments!!.getString("id")!!)
        }
    }
}

private val AnimationSpec = tween<IntOffset>(1000)
private val HalfWidthOffsetPositive: (fullWidth: Int) -> Int = { fullWidth -> fullWidth / 1 }
private val HalfWidthOffsetNegative: (fullWidth: Int) -> Int = { fullWidth -> -fullWidth / 1 }

private object RepoListScreen : Screen(
    route = "repolist",
    name = "Repo List",
)

@Composable
private fun RepoList(onRepoClick: (id: String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Repos") })
        },
        content = {
            RepoList(
                repos = repos,
                onRepoClick = onRepoClick,
            )
        },
    )
}

@Composable
private fun RepoList(repos: List<Repo>?, onRepoClick: (String) -> Unit) {
    val contentPadding = 16.dp
    when {
        repos == null -> Text(
            "Loading...",
            modifier = Modifier.padding(contentPadding),
        )
        repos.isEmpty() -> Text(
            "No repos here",
            modifier = Modifier.padding(contentPadding),
        )
        else -> LazyColumn(
            contentPadding = PaddingValues(contentPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(repos) { repo ->
                RepoItem(
                    repo = repo,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onRepoClick(repo.id) },
                )
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
            .padding(4.dp),
    )
}

@Preview @Composable
private fun PreviewRepoListScreen() {
    RepoList(
        repos = repos,
        onRepoClick = {},
    )
}

private data class RepoItem(
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
    imageUri = owner.imageUri,
)
