package com.ifucolo.rickandmorty.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val origin: OriginDto,
    val image: String,
    val episode: List<String>
)

@Serializable
data class OriginDto(val name: String)