package com.ifucolo.rickandmorty.domain

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val id: Int,
    val name: String,
    val airDateDisplay: String,
    val code: String,
    val characterIds: List<CharacterId>
)

@Serializable
class CharacterId(val value: Int)