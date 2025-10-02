package com.ifucolo.rickandmorty.data.local.source

import com.ifucolo.rickandmorty.data.local.dao.PageRemoteKeysDao
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.dto.OperationResult
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import javax.inject.Inject

interface PageRemoteKeyLocalDataSource {
    suspend fun getRemoteKeys(label: String): LocalResult<PageRemoteKeysEntity>
    suspend fun upsert(keys: PageRemoteKeysEntity): OperationResult
    suspend fun clear(label: String): OperationResult
}

class PageRemoteKeyLocalDataSourceImpl @Inject constructor(
    private val pageRemoteKeysDao: PageRemoteKeysDao
): PageRemoteKeyLocalDataSource {

    override suspend fun getRemoteKeys(label: String): LocalResult<PageRemoteKeysEntity> =
        runCatching {
            pageRemoteKeysDao.getRemoteKeys(label = label)
        }.fold(
            onSuccess = { keys ->
                if (keys != null) {
                    LocalResult.Success(keys)
                } else {
                    LocalResult.Empty
                }
            },
            onFailure = { LocalResult.Error(it) }
        )

    override suspend fun upsert(keys: PageRemoteKeysEntity): OperationResult =
        runCatching {
            pageRemoteKeysDao.upsert(keys = keys)
        }.fold(
            onSuccess = { OperationResult.Success },
            onFailure = { OperationResult.Error(it) }
        )


    override suspend fun clear(label: String): OperationResult =
        runCatching {
            pageRemoteKeysDao.clear(label = label)
        }.fold(
            onSuccess = { OperationResult.Success },
            onFailure = { OperationResult.Error(it) }
        )
}