package ru.gas.humblr.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gas.humblr.domain.model.LoadingState
import ru.gas.humblr.domain.model.SubredditListItem
import ru.gas.humblr.domain.usecase.GetSubscribed
import ru.gas.humblr.domain.usecase.GetNewSubreddits
import ru.gas.humblr.domain.usecase.GetPopularSubreddits
import ru.gas.humblr.domain.usecase.GetUnsubscribed
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewSubreddits: GetNewSubreddits,
    private val getPopularSubreddits: GetPopularSubreddits,
    private val getSubscribed: GetSubscribed,
    private val getUnsubscribed: GetUnsubscribed,
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState<Any?>>(LoadingState.Loading())
    val loadingState = _loadingState.asStateFlow()

    private val _newSubreddits = MutableStateFlow<List<SubredditListItem>>(emptyList())
    val newSubreddits = _newSubreddits.asStateFlow()
    private val _popularSubreddits = MutableStateFlow<List<SubredditListItem>>(emptyList())
    val popularSubreddits = _popularSubreddits.asStateFlow()
//    private val _isUserSubscribed = MutableStateFlow(false)
//    val isUserSubscribed = _isUserSubscribed.asStateFlow()

    fun loadPopularSubreddits() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = LoadingState.Loading()
            kotlin.runCatching {
                getPopularSubreddits()
            }.onSuccess {
                _loadingState.value = LoadingState.Success()
                _newSubreddits.value = it
            }.onFailure {
                Log.d("eee","on Failure ${it.message}")
            }
        }
    }

    fun loadNewSubreddits() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = LoadingState.Loading()
            kotlin.runCatching {
                getNewSubreddits()
            }.onSuccess {
                _loadingState.value = LoadingState.Success()
                _newSubreddits.value = it
            }.onFailure {
                Log.d("eee","on Failure ${it.message}")
            }
        }
    }


    fun getUserSubscribed(subreddit: String) {
        Log.d("Auth", "change subscription in vm ${_newSubreddits.value}")
        viewModelScope.launch(Dispatchers.IO) {
            getSubscribed(subreddit)
        }
    }

    fun getUserUnsubscribed(subreddit: String) {
        Log.d("Auth", "change subscription in vm ${_newSubreddits.value}")
        viewModelScope.launch(Dispatchers.IO) {
            getUnsubscribed(subreddit)
        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}