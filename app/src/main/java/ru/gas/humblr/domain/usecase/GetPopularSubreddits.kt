package ru.gas.humblr.domain.usecase

import ru.gas.humblr.data.remote.RemoteRepository
import ru.gas.humblr.domain.model.SubredditListItem
import javax.inject.Inject

class GetPopularSubreddits @Inject constructor(private val remoteRepository: RemoteRepository) {
    suspend operator fun invoke(): List<SubredditListItem> {
        return remoteRepository.getPopularSubreddits()
    }
}