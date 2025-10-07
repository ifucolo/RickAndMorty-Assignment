package com.ifucolo.rickandmorty.data.repository.mock

import com.ifucolo.rickandmorty.domain.CharacterDetail
import com.ifucolo.rickandmorty.domain.Episode


fun characterDetail(
    id: Int = 1,
    name: String = "Rick Sanchez",
    status: String = "Alive",
    species: String = "Human",
    originName: String = "Earth (C-137)",
    imageUrl: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    episodeCount: Int = 51
): CharacterDetail {
    return CharacterDetail(
        id = id,
        name = name,
        status = status,
        species = species,
        originName = originName,
        imageUrl = imageUrl,
        episodeCount = episodeCount
    )
}

fun episode(
    id: Int = 1,
    name: String = "Pilot",
    airDateDisplay: String = "02/12/2013",
    code: String = "S01E01",
    characterIds: List<Int> = listOf(1, 2, 3)
): Episode {
    return Episode(
        id = id,
        name = name,
        airDateDisplay = airDateDisplay,
        code = code,
        characterIds = characterIds
    )
}
