package com.ifucolo.rickandmorty.data.remote.source

import com.ifucolo.rickandmorty.data.remote.api.RickAndMortyApi
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.dto.CharacterDto
import com.ifucolo.rickandmorty.data.remote.dto.EpisodePageDto
import javax.inject.Inject

interface RickAndMortyRemoteDataSource {
    suspend fun getEpisodes(page: Int): ApiResult<EpisodePageDto>
    suspend fun getCharacter(id: Int): ApiResult<CharacterDto>
}

class RickAndMortyRemoteDataSourceImpl @Inject constructor(
    private val api: RickAndMortyApi
) : RickAndMortyRemoteDataSource {
    override suspend fun getEpisodes(page: Int): ApiResult<EpisodePageDto> =
        runCatching { api.getEpisodes(page = page) }
            .fold(
                onSuccess = {
                    ApiResult.Success(it)
                },
                onFailure = {
                    ApiResult.Error(it)
                }
            )

    override suspend fun getCharacter(id: Int): ApiResult<CharacterDto> =
        runCatching { api.getCharacter(id = id) }
            .fold(
                onSuccess = {
                    ApiResult.Success(it)
                },
                onFailure = {
                    ApiResult.Error(it)
                }
            )
}