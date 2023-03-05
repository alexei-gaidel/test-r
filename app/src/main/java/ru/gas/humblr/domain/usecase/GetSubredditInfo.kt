package ru.gas.humblr.domain.usecase

import android.util.Log
import ru.gas.humblr.data.remote.RemoteRepository
import ru.gas.humblr.domain.model.Subreddit
import javax.inject.Inject

class GetSubredditInfo @Inject constructor(private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(subreddit: String): Subreddit {
        Log.d("eee", "remoteRepository.getSingleSubreddit(subreddit) -- subredditId $subreddit")
        return remoteRepository.getSingleSubreddit(subreddit)

    }

}