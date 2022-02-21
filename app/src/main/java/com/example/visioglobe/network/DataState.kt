package com.example.visioglobe.network


sealed class DataState<T> {
    class Success<T>(val data: T) : DataState<T>()
    class Error<T>(val message: String?, val data: T? = null) : DataState<T>()
    class Loading<T> : DataState<T>()
    class Nothing<T> : DataState<T>()
}
