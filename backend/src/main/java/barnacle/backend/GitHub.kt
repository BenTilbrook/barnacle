package barnacle.backend

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.Instant
import javax.inject.Singleton

@JsonClass(generateAdapter = true)
data class Repo(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "full_name")
    val fullName: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "stargazers_count")
    val starCount: Int,

    @Json(name = "owner")
    val owner: User,
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: String,

    @Json(name = "login")
    val login: String,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "location")
    val location: String? = null,

    @Json(name = "bio")
    val bio: String? = null,

    @Json(name = "followers")
    val followerCount: Int = 0,

    @Json(name = "following")
    val followingCount: Int = 0,

    @Json(name = "avatar_url")
    val imageUri: HttpUrl? = null,
)

@JsonClass(generateAdapter = true)
data class Commit(
    @Json(name = "sha")
    val sha: String,

    @Json(name = "commit")
    val info: Info,

    @Json(name = "author")
    val author: User,
) {
    @JsonClass(generateAdapter = true)
    data class Info(
        @Json(name = "message")
        val message: String?,

        @Json(name = "comment_count")
        val commentCount: Int = 0,
    )

    val message get() = info.message
}

@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "created_at")
    val createdAt: Instant,
)

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "items")
    val repos: List<Repo>,
)

interface Api {
    @GET("repos/{owner}/{name}")
    suspend fun repo(@Path("owner") owner: String, @Path("name") name: String): Repo

    @GET("repos/{owner}/{repo}/commits")
    suspend fun commits(@Path("owner") owner: String, @Path("repo") repo: String): List<Commit>

    @GET("repos/{owner}/{repo}/commits/{sha}")
    suspend fun commit(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("sha") sha: String,
    ): Commit

    @GET("users/{username}")
    suspend fun user(@Path("username") username: String): User

    @GET("users/{username}/events/public")
    suspend fun events(@Path("username") username: String): List<Event>

    @GET("search/repositories?q=square")
    suspend fun search(): SearchResult
}

@Module
@InstallIn(SingletonComponent::class)
internal object GitHubModule {
    @Provides @Singleton
    fun api(okHttpClient: OkHttpClient, moshi: Moshi): Api = Retrofit.Builder()
        .callFactory(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.github.com/")
        .build()
        .create(Api::class.java)
}
