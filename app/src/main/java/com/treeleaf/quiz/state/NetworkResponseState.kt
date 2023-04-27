package com.treeleaf.quiz.state

sealed class NetworkResponseState<T>(val data: T? = null,val message: String? = null) {
    class Loading<T>: NetworkResponseState<T>()
    class Success<T>(data: T? = null): NetworkResponseState<T>(data)
    class Error<T>(message: String?): NetworkResponseState<T>(message = message)
}
