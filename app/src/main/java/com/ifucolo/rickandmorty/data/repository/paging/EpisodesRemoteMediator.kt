// data/repo/EpisodesRemoteMediator.kt
package com.ifucolo.rickandmorty.data.repository.paging

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mapper.toEntity
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val EPISODES_FEED_KEY = "episodes"

@OptIn(ExperimentalPagingApi::class)
class EpisodesRemoteMediator @Inject constructor(
    private val db: RickAndMortyDb,
    private val rickAndMortyRemoteDataSource: RickAndMortyRemoteDataSource,
    private val pageRemoteKeyLocalDataSource: PageRemoteKeyLocalDataSource,
    private val rickAndMortyLocalDataSource: RickAndMortyLocalDataSource
) : RemoteMediator<Int, EpisodeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeEntity>
    ): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val nextPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val keysResult = pageRemoteKeyLocalDataSource.getRemoteKeys(EPISODES_FEED_KEY)
                    when(keysResult) {
                        is LocalResult.Success -> keysResult.data.nextPage
                        is LocalResult.Empty -> null
                        is LocalResult.Error -> throw keysResult.error
                    } ?: return@withContext MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val pageResult = when (val res = rickAndMortyRemoteDataSource.getEpisodes(nextPage)) {
                is ApiResult.Success -> res.data
                is ApiResult.Error -> throw res.error
            }

            val entities = pageResult.results.map { it.toEntity() }
            val next = pageParam(pageResult.info.next)
            val endReached = (pageResult.info.next == null) || entities.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    rickAndMortyLocalDataSource.clearEpisodes()
                    pageRemoteKeyLocalDataSource.clear(EPISODES_FEED_KEY)
                }
                rickAndMortyLocalDataSource.insertEpisodes(entities)
                pageRemoteKeyLocalDataSource.upsert(
                    PageRemoteKeysEntity(
                        label = EPISODES_FEED_KEY,
                        nextPage = next,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }

    private fun pageParam(nextUrl: String?): Int? =
        nextUrl?.toUri()?.getQueryParameter("page")?.toIntOrNull()
}