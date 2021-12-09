package barnacle.repolist

import barnacle.backend.Repo
import barnacle.backend.User

val repos = List(100) {
    Repo(
        id = "repo-$it",
        name = "Repo $it",
        fullName = "Full repo name $it",
        description = "This is the repo description of repo $it",
        starCount = it * 10,
        owner = User(
            id = "user-$it",
            login = "user-$it",
            name = "User $it",
            location = "Location $it",
            bio = "Bio #it",
            followerCount = 1,
            followingCount = 2,
            imageUri = null,
        ),
    )
}
