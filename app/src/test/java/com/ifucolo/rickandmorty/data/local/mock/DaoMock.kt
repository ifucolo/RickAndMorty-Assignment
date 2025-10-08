package com.ifucolo.rickandmorty.data.local.mock

import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity


fun characterEntity(
    id: Int = 1,
    name: String = "Rick"
) = CharacterEntity(
    id = id, name = name, status = "Alive", species = "Human",
    originName = "Earth", imageUrl = "https://example.com/summer.png", episodeCount = 51
)

fun episodeEntity(id: Int, name: String? = null, characters: List<Int> = listOf(1, 2)) = EpisodeEntity(
    id = id,
    name = name?: "Ep $id",
    code = "S01E$id",
    airDate = "December 2, 2013",
    characterIds = characters
)

fun episodesList(range: IntRange) = range.map { episodeEntity(it) }

val pageRemoteKeysEntity = PageRemoteKeysEntity(label = "episodes", nextPage = 2, lastUpdated = 100L)
