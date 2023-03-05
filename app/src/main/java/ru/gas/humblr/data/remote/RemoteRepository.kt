package ru.gas.humblr.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import ru.gas.humblr.data.remote.models.PostCommentsDto
import ru.gas.humblr.data.remote.utils.PostCommentsResponseDeserializer
import ru.gas.humblr.domain.model.*
import javax.inject.Inject


class RemoteRepository @Inject constructor(private val redditApi: RedditApi) {


    suspend fun getFormattedPostComments(post: String): PostCommentsDto {
        val rawResponse = redditApi.getRawPostComments(post)
        Log.d("eee", "rawResponse:" + rawResponse)
        val formattedResponse = rawResponse.replace("\"replies\": \"\"", "\"replies\": null")
//            .onEach { it.replace("\"replies\": \"\"","\"replies\": null")}
        Log.d("eee", "formattedResponse:" + formattedResponse)
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            PostCommentsDto::class.java,
            PostCommentsResponseDeserializer()
        )
        val gson = gsonBuilder.create()
//        val listType: Type = object : TypeToken<ArrayList<PostCommentsDto?>?>() {}.type
//        val typeOfT = TypeToken.getParameterized(List::class.java, PostCommentsDto::class.java).type
//        val listType = object : TypeToken<List<PostCommentsDto?>?>() {}.type

//        val yourClassList = gson.fromJson(formattedResponse, listType)
//        val videos: List<PostCommentsDto> = gson.fromJson(formattedResponse, object : TypeToken<List<PostCommentsDto?>?>() {}.type)

        return gson.fromJson(formattedResponse, PostCommentsDto::class.java)
    }

    suspend fun getPost(post: String): PostItem {
    return getFormattedPostComments(post).toPostItem()
    }

    //
    suspend fun getPostComments(post: String): List<CommentListItem> {

        val postComments = getFormattedPostComments(post).toCommentList()
        Log.d("eee", "commentsList inside onEach repo $postComments")
        return postComments


//
//
//    suspend fun getPostComments(post: String): List<CommentListItem> {
//
//        Log.d("eee","getPostComments in repo")
//        var commentsList = listOf<CommentListItem>()
//
//        redditApi.getRawPostComments(post).onEach {
//            Log.d("eee","postCommentsDto in repo $it")
//            commentsList =  it.toCommentListItem()
//            Log.d("eee","commentsList inside onEach repo $commentsList")
//        }
//        Log.d("eee","commentsList in repo $commentsList")
//        return commentsList
//
//
//    }


//    suspend fun getPost(post: String): CommentListItem
//    {
//        return redditApi.getPostComments(post).toCommentListItem().first()
    }


    suspend fun getNewSubreddits(): List<SubredditListItem> {
        return redditApi.getNewSubreddits(PAGE_SIZE).toSubredditList().onEach {
            val isSubscribed = getSubscription(it.id)
            it.subscribed = isSubscribed
        }
    }

    suspend fun getPopularSubreddits(): List<SubredditListItem> {
        return redditApi.getPopularSubreddits(PAGE_SIZE).toSubredditList().onEach {
            val isSubscribed = getSubscription(it.id)
            it.subscribed = isSubscribed
        }
    }

    suspend fun getSubredditPosts(subreddit: String): List<SubredditPostsItem> {
        return redditApi.getSubredditPosts(subreddit).toSubredditPostsList()
    }

    suspend fun getSingleSubreddit(id: String): Subreddit {

        val subreddit = redditApi.getSubreddit(id).toSubreddit()
        Log.d("eee", "subreddit in remote repo -- $subreddit")
        return subreddit
    }

    suspend fun getSubscription(subreddit: String): Boolean {
        return redditApi.getSubreddit(subreddit).data.userIsSubscriber
    }

    suspend fun getSubscribed(subreddit: String) {
        Log.d("Auth", "subscribe repo")
        return redditApi.subscribe(subreddit)
    }

    suspend fun getUnsubscribed(subreddit: String) {
        Log.d("Auth", "unsubscribe repo")
        return redditApi.unsubscribe(subreddit)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}