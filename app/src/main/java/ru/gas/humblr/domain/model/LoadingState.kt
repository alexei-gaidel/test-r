package ru.gas.humblr.domain.model

sealed class LoadingState<T>(val message: String? = null) {
    class Loading<T> : LoadingState<T>()
    class Success<T> : LoadingState<T>()
    class Error<T>(message: String?): LoadingState<T>(message)
}
