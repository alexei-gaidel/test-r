package ru.gas.humblr.data.remote.models

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.gas.humblr.data.remote.Preview
import ru.gas.humblr.domain.model.CommentListItem
import ru.gas.humblr.domain.model.PostItem

//@JsonClass(generateAdapter = true)
//class PostCommentsResponse(
//    val postResponse: PostCommentsDto,
//    val commentsResponse: PostCommentsDto
//)

@JsonClass(generateAdapter = true)
data class PostCommentsDto(
    val data: PostData?
) {
    fun toPostItem(): PostItem {

        val post = this.data?.children?.first()
        Log.d("eee", "this.data?.children? ${this.data?.children}")

       post.apply {
            return PostItem(
                author = this?.data?.author,
                title = this?.data?.title,
                body = this?.data?.selftext,
                image = this?.data?.preview?.images?.first()?.source?.url,
                score = this?.data?.score,
                created = this?.data?.created,
                numComments = this?.data?.num_comments
            )
           Log.d("eee", "post in dto $post")
        }
    }

    fun toCommentList(): List<CommentListItem> {

        val comments = this.data?.children


        val allComments = mutableListOf<Comment>()

        comments?.forEach {

            allComments.addAll(getReplies(it))
        }
        return allComments.map {
            CommentListItem(
                author = it.data.author,
//                title = it.data.title,
                body = it.data.body,
//                image = it.data.postImg,
                score = it.data.score,
//                created = it.data.createdUTC
            )
        }
//        return allComments.toList()
    }

    private fun getReplies(comment: Comment): List<Comment> {
        val allComments = mutableListOf<Comment>()
        allComments.add(comment)
        comment.data.replies?.data?.children?.let { replies ->
            if (replies.isNotEmpty()) {
                replies.forEach { comment ->
                    allComments.addAll(getReplies(comment))
                }
            }
        }
        return allComments
    }
}


@JsonClass(generateAdapter = true)
data class PostData(
    val children: List<Comment> = listOf(),
    )

@JsonClass(generateAdapter = true)
data class Comment(
    val data: CommentData
)

@JsonClass(generateAdapter = true)
data class CommentData(
//    @Json(name = "num_comments")
//    val numComments: Long?,


//    val subreddit: String?,

//    val saved: Boolean?,

    val title: String? = null,
    val selftext: String? = null,

    @Json(name = "subreddit_name_prefixed")
    val subredditNamePrefixed: String?,

//    @Json(name = "num_comments")
    val num_comments: Long?,
//
//    @Json(name = "gallery_data")
//    val galleryData: GalleryData? = null,

    val score: Long?,

//    val id: String,

    val author: String?,



    val permalink: String?,

    @Json(name = "url")
    val postImg: String? = null,

    @Json(name = "created_utc")
    val created: Double?,

    val replies: Replies?,

    val body: String? = null,
    val preview: Preview?
    )

@JsonClass(generateAdapter = true)
data class Replies(
    var data: PostData? = PostData()

)
