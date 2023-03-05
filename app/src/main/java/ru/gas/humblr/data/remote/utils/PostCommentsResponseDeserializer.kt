package ru.gas.humblr.data.remote.utils

import com.google.gson.*
import ru.gas.humblr.data.remote.models.PostCommentsDto
import java.lang.reflect.Type

internal class PostCommentsResponseDeserializer : JsonDeserializer<PostCommentsDto?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PostCommentsDto {
        val gson = Gson()
        val jsonArray = json.asJsonArray
        return gson.fromJson(jsonArray[0], PostCommentsDto::class.java)
//        val commentsResponse = gson.fromJson(jsonArray[1], PostCommentsDto::class.java)
//        return PostCommentsResponse(postResponse, commentsResponse)
    }
}