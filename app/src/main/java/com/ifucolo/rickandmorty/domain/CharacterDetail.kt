package com.ifucolo.rickandmorty.domain

data class CharacterDetail(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val originName: String,
    val imageUrl: String,
    val episodeCount: Int
)