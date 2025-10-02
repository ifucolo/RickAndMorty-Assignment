package com.ifucolo.rickandmorty.domain

data class Episode(
    val id: Int,
    val name: String,
    val airDateDisplay: String,
    val code: String,
    val characterIds: List<Int>
)