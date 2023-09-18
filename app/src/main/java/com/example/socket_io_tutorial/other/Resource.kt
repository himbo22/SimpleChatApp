package com.example.socket_io_tutorial.other

sealed class Resource<T> {
    class Success<T>(val data: T? = null) : Resource<T>()

    class Error<T> (val data : T? = null) : Resource<T>()
}