package com.ifucolo.rickandmorty.data.local.source

import androidx.paging.PagingSource
import com.ifucolo.rickandmorty.data.local.dao.CharacterDao
import com.ifucolo.rickandmorty.data.local.dao.EpisodeDao
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.dto.OperationResult
import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import javax.inject.Inject

interface RickAndMortyLocalDataSource {
    // Episode operations
    fun episodesPagingSource(): PagingSource<Int, EpisodeEntity>
    suspend fun insertEpisodes(items: List<EpisodeEntity>): OperationResult
    suspend fun clearEpisodes(): OperationResult

    // Character operations
    suspend fun getCharacter(id: Int): LocalResult<CharacterEntity>
    suspend fun upsertCharacter(entity: CharacterEntity): OperationResult
}

class RickAndMortyLocalDataSourceImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val episodeDao: EpisodeDao
): RickAndMortyLocalDataSource {

    // Episode operations
    override fun episodesPagingSource(): PagingSource<Int, EpisodeEntity> =
        episodeDao.pagingSource()

    override suspend fun insertEpisodes(items: List<EpisodeEntity>): OperationResult =
        runCatching {
            episodeDao.insertAll(items)
        }.fold(
            onSuccess = {
                OperationResult.Success
            },
            onFailure = { error ->
                OperationResult.Error(error)
            }
        )

    override suspend fun clearEpisodes(): OperationResult = runCatching {
        episodeDao.clearAll()
    }.fold(
        onSuccess = {
            OperationResult.Success
        },
        onFailure = { error ->
            OperationResult.Error(error)
        }
    )

    // Character operations
    override suspend fun getCharacter(id: Int): LocalResult<CharacterEntity> = runCatching {
        characterDao.getById(id)
    }.fold(
        onSuccess = { character ->
            if (character != null) {
                LocalResult.Success(character)
            } else {
                LocalResult.Empty
            }
        },
        onFailure = { error ->
            LocalResult.Error(error)
        }
    )

    override suspend fun upsertCharacter(entity: CharacterEntity): OperationResult = runCatching {
        characterDao.upsert(entity)
    }.fold(
        onSuccess = {
            OperationResult.Success
        },
        onFailure = { error ->
            OperationResult.Error(error)
        }
    )
}