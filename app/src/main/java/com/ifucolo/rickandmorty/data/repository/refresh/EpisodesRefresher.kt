package com.ifucolo.rickandmorty.data.repository.refresh

import androidx.room.withTransaction
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeys
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mapper.toEntity
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import javax.inject.Inject


interface EpisodesRefresher {
    suspend fun refresh(pages: Int = 2)
}
class EpisodesRefresherImpl @Inject constructor(
    private val db: RickAndMortyDb,
    private val remote: RickAndMortyRemoteDataSource,
    private val local: RickAndMortyLocalDataSource,
    private val pageKeys: PageRemoteKeyLocalDataSource
): EpisodesRefresher {

    override suspend fun refresh(pages: Int) {
        val remotePageKey = PageRemoteKeys.EPISODES_FEED_KEY.key
        var nextPage: Int? = 1

        db.withTransaction {
            local.clearEpisodes()
            pageKeys.clear(label = remotePageKey)

            repeat(pages) {
                val page = nextPage ?: return@repeat
                val pageResultRemote = when (val res = remote.getEpisodes(page)) {
                    is ApiResult.Success -> res.data
                    is ApiResult.Error -> throw res.error
                }

                val entities = pageResultRemote.results.map { it.toEntity() }
                if (entities.isNotEmpty()) {
                    local.insertEpisodes(entities)
                }

                nextPage = pageResultRemote.nextPage()

                pageKeys.upsert(
                    PageRemoteKeysEntity(
                        label = remotePageKey,
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )

                if (nextPage == null) return@repeat
            }
        }
    }
}