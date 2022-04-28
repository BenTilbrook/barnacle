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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.launchMolecule
import barnacle.backend.Repo
import barnacle.core.Screen
import barnacle.core.Tab
import barnacle.repodetail.RepoDetail
import barnacle.repodetail.RepoDetailScreen
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            RepoListScreen(navController)
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
private val HalfWidthOffsetPositive: (fullWidth: Int) -> Int = { fullWidth -> +fullWidth / 1 }
private val HalfWidthOffsetNegative: (fullWidth: Int) -> Int = { fullWidth -> -fullWidth / 1 }

private object RepoListScreen : Screen(
    route = "repolist",
    name = "Repo List",
)

@Composable
private fun RepoListScreen(navController: NavController) {
    val scope = rememberCoroutineScope { AndroidUiDispatcher.Main }
    val presenter: RepoListPresenter = null // TODO: Inject presenter
    val actions = remember { MutableSharedFlow<RepoListAction>() }
    val state = remember { scope.launchMolecule { presenter(actions) } }
    RepoList(
        state = state,
        onRefresh = { actions.tryEmit(RepoListAction.Refresh) },
        onRepoClick = { id -> navController.navigate(RepoDetailScreen.route(id)) },
    )
}

@Composable
private fun RepoList(
    state: StateFlow<RepoListState>,
    onRefresh: () -> Unit,
    onRepoClick: (id: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Repos") })
        },
        content = {
            // TODO: Swipe refresh
            val contentPadding = 16.dp
            val state by state.collectAsState()
            val repos = state.repos
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
        },
    )
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
        state = MutableStateFlow(RepoListState(repos = repos, isLoading = false)),
        onRefresh = {},
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
