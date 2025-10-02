package com.ifucolo.rickandmorty.data.local.dto

import com.ifucolo.rickandmorty.data.remote.dto.ApiResult

sealed class LocalResult<out T> {
    data class Success<out T>(val data: T) : LocalResult<T>()
    object Empty : LocalResult<Nothing>()
    data class Error(val error: Throwable) : LocalResult<Nothing>()
}

sealed class OperationResult {
    object Success : OperationResult()
    data class Error(val error: Throwable) : OperationResult()
}