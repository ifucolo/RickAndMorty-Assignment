package com.ifucolo.rickandmorty.data.mapper

import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.remote.dto.CharacterDto
import com.ifucolo.rickandmorty.domain.CharacterDetail


fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    originName = origin.name,
    imageUrl = image,
    episodeCount = episode.size
)

fun CharacterEntity.toDomain(): CharacterDetail = CharacterDetail(
    id = id,
    name = name,
    status = status,
    species = species,
    originName = originName,
    imageUrl = imageUrl,
    episodeCount = episodeCount
)