package ru.gas.humblr.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Response
import ru.gas.humblr.data.remote.models.PostCommentsDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gas.humblr.data.remote.models.RedditUserDto
import ru.gas.humblr.data.remote.utils.PostCommentsResponseDeserializer

interface RedditApi {
    @GET("api/v1/me")
    suspend fun getCurrentUser(
    )
    : RedditUserDto

    @GET("r/all/new?raw_json=1")
    suspend fun getNewSubreddits(@Query("limit") pageSize: Int)
            : SubredditListItemDto

    @GET("r/all/top?raw_json=1")
    suspend fun getPopularSubreddits(@Query("limit") pageSize: Int)
            : SubredditListItemDto

    @GET("/r/{subreddit}/about")
    suspend fun getSubreddit(@Path("subreddit") subreddit: String): SubredditDto

    @POST("/api/subscribe?action=sub")
    suspend fun subscribe(@Query("sr_name") id: String)

    @POST("/api/subscribe?action=unsub")
    suspend fun unsubscribe(@Query("sr_name") id: String)

    @GET("/r/{subreddit}?raw_json=1")
    suspend fun getSubredditPosts(@Path("subreddit") subreddit: String): SubredditPostsDto

    @GET("/comments/{post}?raw_json=1")
    suspend fun getPostComments(@Path("post") post: String) : List<PostCommentsDto>

    @GET("/comments/{post}?raw_json=1")
    suspend fun getRawPostComments(@Path("post") post: String) : String
//




//    @GET("photos")
//    suspend fun getPhotos(@Query("orderBy") orderBy: String, @Query("page") page: Int): List<PhotoItem>
//
//    @GET("/users/{username}/likes")
//    suspend fun getLikedPhotos(@Path("username") userName: String, @Query("per_page") pageSize: Int): List<PhotoItem>
//
//    @GET("search/photos")
//    suspend fun searchPhotos(@Query("query") query: String): SearchResults
//
//    @GET("/collections")
//    suspend fun getCollections(): List<PhotoCollection>
//
//    @GET("/collections/{id}")
//    suspend fun getSingleCollection(@Path("id") id: String): CollectionInfo
//
//    @GET("/collections/{id}/photos")
//    suspend fun getCollectionPhotos(@Path("id") id: String): List<PhotoItem>
//
//    @GET("/photos/{id}")
//    suspend fun getSinglePhoto(@Path("id") id: String): PhotoDetails
//
//    @POST("/photos/{id}/like")
//    suspend fun likePhoto(@Path("id") id: String): PhotoLikes
//
//    @DELETE("/photos/{id}/like")
//    suspend fun unlikePhoto(@Path("id") id: String): PhotoLikes
companion object {
    val BASE_URL= "https://oauth.reddit.com"
}

}
