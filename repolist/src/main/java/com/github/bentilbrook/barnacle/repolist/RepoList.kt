package com.github.bentilbrook.barnacle.sample.repolist

import android.net.Uri
import com.github.bentilbrook.barnacle.sample.backend.Repo

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
