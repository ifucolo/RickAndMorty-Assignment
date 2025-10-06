package com.ifucolo.rickandmorty.domain

sealed class DomainResult<out T> {
    object Loading : DomainResult<Nothing>()
    object Empty : DomainResult<Nothing>()
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val error: Throwable) : DomainResult<Nothing>()
}