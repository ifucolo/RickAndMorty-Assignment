package com.ifucolo.rickandmorty.data.remote.dto

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: Throwable) : ApiResult<Nothing>()
}