package ru.gas.humblr.domain.usecase

import ru.gas.humblr.data.remote.RemoteRepository
import ru.gas.humblr.domain.model.CommentListItem
import javax.inject.Inject

class GetPostComments @Inject constructor(private val remoteRepository: RemoteRepository) {
    suspend operator fun invoke(post: String)
            : List<CommentListItem> {
        return remoteRepository.getPostComments(post)
    }
}
