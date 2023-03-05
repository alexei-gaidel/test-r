package ru.gas.humblr.data.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditUserDto(
//    val created: Double,
    val icon_img: String,
    val id: String,
//    val inbox_count: Int,
    val name: String,
    val num_friends: Int,
//    val snoovatar_img: String,
//    val snoovatar_size: Any,
//    val subreddit: Subreddit,
//    val total_karma: Int,
)