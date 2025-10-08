package com.ifucolo.rickandmorty.data.mapper

import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.remote.dto.EpisodeDto
import com.ifucolo.rickandmorty.domain.CharacterId
import com.ifucolo.rickandmorty.domain.Episode
import com.ifucolo.rickandmorty.util.characterIdsFromUrls
import com.ifucolo.rickandmorty.util.rnmAirDateToDdMMyyyy

fun EpisodeDto.toEntity(): EpisodeEntity = EpisodeEntity(
    id = id,
    name = name,
    airDate = rnmAirDateToDdMMyyyy(airDate),
    code = episode,
    characterIds = characterIdsFromUrls(characters)
)

fun EpisodeEntity.toDomain(): Episode = Episode(
    id = id,
    name = name,
    airDateDisplay = airDate,
    code = code,
    characterIds = characterIds.map { CharacterId(it) }
)