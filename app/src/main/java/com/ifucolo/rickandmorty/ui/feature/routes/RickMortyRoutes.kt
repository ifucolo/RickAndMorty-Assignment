package com.ifucolo.rickandmorty.ui.feature.routes

import kotlinx.serialization.Serializable


@Serializable
sealed class RickMortyRoutes {
    @Serializable
    data object EpisodeList : RickMortyRoutes()

    @Serializable
    data class EpisodeDetail(val title: String, val charactersIds: List<Int>) : RickMortyRoutes()

    @Serializable
    data class CharacterDetail(val characterId: Int) : RickMortyRoutes()
}