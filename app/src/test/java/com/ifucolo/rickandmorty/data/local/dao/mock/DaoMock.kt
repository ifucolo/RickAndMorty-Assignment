package com.ifucolo.rickandmorty.data.local.dao.mock

import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity


val characterEntity = CharacterEntity(
    id = 1, name = "Rick", status = "Alive", species = "Human",
    originName = "Earth", imageUrl = "", episodeCount = 51
)

fun episodeEntity(id: Int) = EpisodeEntity(
    id = id,
    name = "Ep $id",
    code = "S01E$id",
    airDate = "December 2, 2013",
    characterIds = listOf(1, 2)
)

fun episodesList(range: IntRange) = range.map { episodeEntity(it) }

val pageRemoteKeysEntity = PageRemoteKeysEntity(label = "episodes", nextPage = 2, lastUpdated = 100L)
