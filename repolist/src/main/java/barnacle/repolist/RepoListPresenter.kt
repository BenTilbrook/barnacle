package barnacle.repolist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import barnacle.backend.Api
import barnacle.backend.Repo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepoListPresenter @Inject constructor(private val api: Api) {
    @Composable
    operator fun invoke(actions: Flow<RepoListAction>): RepoListState {
        var repos by remember { mutableStateOf<List<Repo>?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
            isLoading = true
            try {
                repos = api.search().repos
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                TODO()
            } finally {
                isLoading = false
            }
        }
        return RepoListState(repos = repos, isLoading = isLoading)
    }
}
