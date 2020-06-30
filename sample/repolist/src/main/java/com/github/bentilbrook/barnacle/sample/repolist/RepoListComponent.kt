package com.github.bentilbrook.barnacle.sample.repolist

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.bentilbrook.barnacle.Action
import com.github.bentilbrook.barnacle.Dispatcher
import com.github.bentilbrook.barnacle.Epic
import com.github.bentilbrook.barnacle.Module
import com.github.bentilbrook.barnacle.sample.backend.Api
import com.github.bentilbrook.barnacle.sample.backend.Repo
import com.github.bentilbrook.barnacle.sample.core.layoutInflater
import com.github.bentilbrook.barnacle.sample.repolist.databinding.RepoListComponentBinding
import com.github.bentilbrook.barnacle.sample.repolist.databinding.RepoListComponentRepoItemBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

fun RepoListComponent(
    state: Flow<RepoListState>,
    dispatch: Dispatcher,
    parent: ViewGroup,
    scope: CoroutineScope
) {
    val binding = RepoListComponentBinding.inflate(parent.layoutInflater(), parent, true)

    // Toolbar
    binding.toolbar.apply {
        title = "Browse Repositories"
        inflateMenu(R.menu.repo_list_component)
        menu.findItem(R.id.logout).setOnMenuItemClickListener {
            dispatch(RepoListAction.Logout); true
        }
    }

    // Swipe refresh
    binding.swipeRefreshLayout.setOnRefreshListener { dispatch(RepoListAction.Reload) }

    // Repos
    val adapter = RepoAdapter(dispatch)
    state.map { it.repos }
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)
        .map { repos -> repos.map { it.toItem() } }
        .onEach { adapter.submitList(it) }
        .launchIn(scope)
    binding.recyclerView.adapter = adapter
}

sealed class RepoListAction : Action {
    internal object Reload : RepoListAction()
    internal object Logout : RepoListAction()
    internal object ReposLoading : RepoListAction()
    internal data class ReposLoaded(val repos: List<Repo>) : RepoListAction()
    internal data class RepoLoadFailed(val exception: Throwable) : RepoListAction()
    internal data class RepoClick(val owner: String, val name: String) : RepoListAction()
}

internal data class RepoItem(
    val id: String,
    val owner: String,
    val name: String,
    val text: String,
    val imageUri: Uri?
)

private fun Repo.toItem() = RepoItem(
    id = id,
    owner = owner.login,
    name = name,
    text = fullName,
    imageUri = owner.imageUri
)

data class RepoListState(
    val isLoading: Boolean = true,
    val repos: List<Repo> = emptyList(),
    val errorMessage: String? = null
)

internal class RepoAdapter(private val dispatch: Dispatcher) :
    ListAdapter<RepoItem, RepoAdapter.RepoViewHolder>(ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepoViewHolder(
            RepoListComponentRepoItemBinding.inflate(parent.layoutInflater(), parent, false)
        )

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.binding.apply {
            // Image
            val item = getItem(position)
            Glide.with(root)
                .load(item.imageUri)
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.image_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(repoImageView)
            // Text
            repoTitleView.text = item.text
        }

    }

    inner class RepoViewHolder(val binding: RepoListComponentRepoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = getItem(layoutPosition)
                dispatch(RepoListAction.RepoClick(owner = item.owner, name = item.name))
            }
        }
    }

    companion object {
        private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<RepoItem>() {
            override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem) =
                oldItem.text == newItem.text && oldItem.imageUri == newItem.imageUri
        }
    }
}

fun repoListReducer(state: RepoListState, action: RepoListAction): RepoListState =
    when (action) {
        is RepoListAction.Reload -> state
        is RepoListAction.Logout -> TODO()
        is RepoListAction.ReposLoading ->
            state.copy(isLoading = true, errorMessage = null)
        is RepoListAction.ReposLoaded ->
            state.copy(isLoading = false, errorMessage = null, repos = action.repos)
        is RepoListAction.RepoLoadFailed ->
            state.copy(isLoading = false, errorMessage = action.exception.toString())
        is RepoListAction.RepoClick -> TODO()
    }

class RepoListEpic @Inject constructor(private val api: Api) : Epic<RepoListState, RepoListAction> {
    override fun invoke(
        actions: Flow<RepoListAction>,
        state: Flow<RepoListState>
    ): Flow<RepoListAction> = merge(
        dataEpic(actions, state)
    )

    private fun dataEpic(
        actions: Flow<RepoListAction>,
        state: Flow<RepoListState>
    ): Flow<RepoListAction> = actions
        .filterIsInstance<RepoListAction.Reload>()
        .onStart { emit(RepoListAction.Reload) }
        .transformLatest {
            try {
                emit(RepoListAction.ReposLoading)
                val result = api.search()
                val repos = withContext(Dispatchers.Default) {
                    result.repos.sortedByDescending(Repo::starCount)
                }
                emit(RepoListAction.ReposLoaded(repos))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                RepoListAction.RepoLoadFailed(e)
            }
        }

}

class RepoListModule @Inject constructor(repoListEpic: RepoListEpic) :
    Module<RepoListState, RepoListState, RepoListAction>(
        selector = { it },
        reducer = ::repoListReducer,
        epic = repoListEpic
    )
