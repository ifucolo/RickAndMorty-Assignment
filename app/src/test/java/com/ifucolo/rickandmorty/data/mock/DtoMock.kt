package com.ifucolo.rickandmorty.data.mock

import com.ifucolo.rickandmorty.data.remote.dto.CharacterDto
import com.ifucolo.rickandmorty.data.remote.dto.EpisodeDto
import com.ifucolo.rickandmorty.data.remote.dto.OriginDto


fun characterDto(
    id: Int = 1,
    name: String = "Rick Sanchez",
    status: String = "Alive",
    species: String = "Human",
    originName: String = "Earth (C-137)",
    image: String = "https://example.com/rick.png",
    episodes: List<String> = listOf(
        "https://rickandmortyapi.com/api/episode/1",
        "https://rickandmortyapi.com/api/episode/2",
        "https://rickandmortyapi.com/api/episode/28"
    )
): CharacterDto = CharacterDto(
    id = id,
    name = name,
    status = status,
    species = species,
    origin = OriginDto(originName),
    image = image,
    episode = episodes
)


fun episodeDto(
    id: Int = 6,
    name: String = "Rick Potion #9",
    airDate: String = "January 27, 2014",   // API format
    episode: String = "S01E06",
    characters: List<String> = listOf(
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2",
        "https://rickandmortyapi.com/api/character/28"
    )
) = EpisodeDto(
    id = id,
    name = name,
    airDate = airDate,
    episode = episode,
    characters = characters
)