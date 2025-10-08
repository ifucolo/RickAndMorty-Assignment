package com.ifucolo.rickandmorty.ui.feature.routes

import com.ifucolo.rickandmorty.domain.CharacterId
import kotlinx.serialization.Serializable


@Serializable
sealed class RickMortyRoutes {
    @Serializable
    data object EpisodeList : RickMortyRoutes()

    @Serializable
    data class EpisodeDetail(val title: String, val charactersIds: List<CharacterId>) : RickMortyRoutes()

    @Serializable
    data class CharacterDetail(val characterId: Int) : RickMortyRoutes()
}