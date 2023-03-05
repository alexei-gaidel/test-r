package ru.gas.humblr.presentation.subreddit_post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gas.humblr.domain.model.CommentListItem
import ru.gas.humblr.domain.model.LoadingState
import ru.gas.humblr.domain.model.PostItem
import ru.gas.humblr.domain.model.SubredditPostsItem
import ru.gas.humblr.domain.usecase.GetPost
import ru.gas.humblr.domain.usecase.GetPostComments
import javax.inject.Inject

@HiltViewModel
class SubredditPostViewModel @Inject constructor(
    private val getPostComments: GetPostComments,
    private val getPost: GetPost
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState<Any?>>(LoadingState.Loading())
    val loadingState = _loadingState.asStateFlow()
    private val _postComments = MutableStateFlow<List<CommentListItem>>(emptyList())
    val postComments = _postComments.asStateFlow()
    private val _post = MutableStateFlow<PostItem?>(null)
    val post = _post.asStateFlow()


    fun loadComments(post: String) {
        Log.d("eee", "loadPostComments loadPostComments")
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = LoadingState.Loading()
            kotlin.runCatching {
                Log.d("eee", "getPostComments")
                getPostComments(post)
            }.onSuccess {
                Log.d("eee", "on Success")
                _loadingState.value = LoadingState.Success()
                _postComments.value = it
            }.onFailure {
                Log.d("eee", "on Failure ${it.message}")
                _loadingState.value = LoadingState.Error("Loading failed")
            }
        }
    }


    fun loadPost(post: String) {
        Log.d("eee", "loadPostComments loadPostComments")
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("eee", "getPostComments")
                getPost(post)
            }.onSuccess {
                Log.d("eee", "on Success")
                _post.value = it
            }.onFailure {
                Log.d("eee", "on Failure ${it.message}")
                _loadingState.value = LoadingState.Error("Loading failed")
            }
        }
    }

//    fun getReplies(comment:Comment):MutableList<Comment>{
//        var allComments = mutableListOf<Comment>()
//        allComments.add(comment)
//        comment.data?.replies?.data?.children?.let { replies->
//            if (replies.isNotEmpty())
//            {
//                replies.forEach { comment ->
//                    allComments.addAll(getReplies(comment))
//                }
//            }
//        }
//        return allComments
//    }
}