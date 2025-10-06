package com.ifucolo.rickandmorty.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeys
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mapper.toEntity
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitor
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject


private const val CACHE_TIMEOUT_HOURS = 1L

@OptIn(ExperimentalPagingApi::class)
class EpisodesRemoteMediator @Inject constructor(
    private val db: RickAndMortyDb,
    private val rickAndMortyRemoteDataSource: RickAndMortyRemoteDataSource,
    private val pageRemoteKeyLocalDataSource: PageRemoteKeyLocalDataSource,
    private val rickAndMortyLocalDataSource: RickAndMortyLocalDataSource,
    private val refreshPrefsDataSource: RefreshPrefsDataSource,
    private val networkMonitor: NetworkMonitor
) : RemoteMediator<Int, EpisodeEntity>() {

    override suspend fun initialize(): InitializeAction {
        val lastRefreshTime = refreshPrefsDataSource.lastRefreshFlow().first()
        val cacheTimeout = TimeUnit.HOURS.toMillis(CACHE_TIMEOUT_HOURS)

        if (System.currentTimeMillis() - lastRefreshTime <= cacheTimeout) {
            return InitializeAction.SKIP_INITIAL_REFRESH
        }

        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeEntity>
    ): MediatorResult = withContext(Dispatchers.IO) {
        val remotePageKey = PageRemoteKeys.EPISODES_FEED_KEY.key
        try {
            if (loadType == LoadType.REFRESH && !networkMonitor.isConnected()) {
                return@withContext MediatorResult.Success(endOfPaginationReached = false)
            }

            val nextPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val keysResult = pageRemoteKeyLocalDataSource.getRemoteKeys(label = remotePageKey)
                    when(keysResult) {
                        is LocalResult.Success -> keysResult.data.nextPage
                        is LocalResult.Empty -> null
                        is LocalResult.Error -> throw keysResult.error
                    } ?: return@withContext MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val pageResultRemote = when (val res = rickAndMortyRemoteDataSource.getEpisodes(nextPage)) {
                is ApiResult.Success -> res.data
                is ApiResult.Error -> throw res.error
            }

            val entities = pageResultRemote.results.map { it.toEntity() }
            val endReached = (pageResultRemote.info.next == null) || entities.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    rickAndMortyLocalDataSource.clearEpisodes()
                    pageRemoteKeyLocalDataSource.clear(label = remotePageKey)
                }
                rickAndMortyLocalDataSource.insertEpisodes(entities)
                pageRemoteKeyLocalDataSource.upsert(
                    PageRemoteKeysEntity(
                        label = remotePageKey,
                        nextPage = pageResultRemote.nextPage(),
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }

            if (loadType == LoadType.REFRESH) {
                runCatching { refreshPrefsDataSource.setLastRefresh(System.currentTimeMillis()) }
            }

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}