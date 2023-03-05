//package ru.gas.humblr.domain.usecase
//
//import ru.gas.humblr.data.remote.RemoteRepository
//import ru.gas.humblr.domain.model.SubredditListItem
//import javax.inject.Inject
//
//class GetSubscription @Inject constructor(private val remoteRepository: RemoteRepository) {
//    suspend operator fun invoke(subreddit: String): Boolean? {
//        return remoteRepository.getSubscription(subreddit)
//    }
//}