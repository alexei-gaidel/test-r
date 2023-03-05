package ru.gas.humblr.presentation.subreddit_description

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gas.humblr.domain.model.Subreddit
import ru.gas.humblr.domain.model.LoadingState
import ru.gas.humblr.domain.usecase.GetSubredditInfo
import ru.gas.humblr.domain.usecase.GetSubscribed
import ru.gas.humblr.domain.usecase.GetUnsubscribed
import javax.inject.Inject
import kotlin.math.ln
import kotlin.math.pow


@HiltViewModel
class SubredditDescriptionViewModel @Inject constructor(
    private val getSubredditInfo: GetSubredditInfo,
    private val getSubscribed: GetSubscribed,
    private val getUnsubscribed: GetUnsubscribed
) :
    ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState<Any?>>(LoadingState.Loading())
    val loadingState = _loadingState.asStateFlow()
    private val _subscribeState = MutableStateFlow(false)
    val subscribeState = _subscribeState.asStateFlow()
    private val _subredditInfo = MutableStateFlow<Subreddit?>(null)
    val subredditInfo = _subredditInfo.asStateFlow()

    fun loadSubredditInfo(id: String): Subreddit? {

        var subreddit: Subreddit? = null
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ee", "runcatching $id")
            kotlin.runCatching {
                getSubredditInfo(id)

            }.onSuccess {
//                Log.d("eee", "_subredditInfo.value ${_subredditInfo.value}")
                Log.d("eee", "it $it")
                _loadingState.value = LoadingState.Success()
                subreddit = it
                _subscribeState.value = it.userIsSubscriber
                _subredditInfo.value = it
            }.onFailure {
                _loadingState.value = LoadingState.Error("Loading failed")
                Log.d("ee", "on Failure")
            }
        }
        return subreddit
    }

    fun getUserSubscribed(subreddit: String) {
        Log.d("eee", "subscribe in vm")
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getSubscribed(subreddit)
            }.onSuccess {
                Log.d("eee", "subscribe success")
                _subscribeState.value = true

            }.onFailure {
                Log.d("eee", "subscribe failure")
            }
        }
    }

    fun getUserUnsubscribed(subreddit: String) {
        Log.d("eee", "unsubscribe in vm")
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getUnsubscribed(subreddit)
            }.onSuccess {
                Log.d("eee", "unsubscribe success")
                _subscribeState.value = false

            }.onFailure {
                Log.d("eee", "unsubscribe failure")
            }
        }
    }


//    fun getFormatedNumber(count: Long): String {
//        if (count < 1000) return "" + count
//        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
//        return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
//    }




}