package com.github.bentilbrook.barnacle.sample.repolist

import android.net.Uri
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.bentilbrook.barnacle.Action
import com.github.bentilbrook.barnacle.Dispatcher
import com.github.bentilbrook.barnacle.sample.backend.Repo
import com.github.bentilbrook.barnacle.sample.core.layoutInflater
import com.github.bentilbrook.barnacle.sample.repolist.databinding.RepoListComponentBinding
import com.github.bentilbrook.barnacle.sample.repolist.databinding.RepoListComponentRepoItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun RepoListComponent(
    state: Flow<RepoListState>,
    dispatch: Dispatcher,
    parent: ConstraintLayout,
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
        .map { repos -> repos.map { it.toItem() } }
        .onEach { adapter.submitList(it) }
        .launchIn(scope)
    binding.recyclerView.adapter = adapter
}

sealed class RepoListAction : Action {
    object Reload : RepoListAction()
    object Logout : RepoListAction()
    data class RepoClick(val owner: String, val name: String) : RepoListAction()
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
                oldItem.text == newItem.text
        }
    }
}

fun repoListReducer(state: RepoListState, action: RepoListAction): RepoListState =
    when (action) {
        is RepoListAction.Reload -> TODO()
        is RepoListAction.Logout -> TODO()
        is RepoListAction.RepoClick -> TODO()
    }

fun repoListEpic(actions: Flow<RepoListAction>, state: Flow<RepoListState>): Flow<RepoListAction> {
    // TODO
    return emptyFlow()
}
