package barnacle.repolist

import androidx.compose.runtime.Immutable
import barnacle.backend.Repo

@Immutable
data class RepoListState(
    val repos: List<Repo>?,
    val isLoading: Boolean,
)

sealed interface RepoListAction {
    object Refresh : RepoListAction
}
