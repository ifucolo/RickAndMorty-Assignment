package com.ifucolo.rickandmorty.data.repository.episodes

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mapper.toDomain
import com.ifucolo.rickandmorty.data.repository.paging.EpisodesRemoteMediator
import com.ifucolo.rickandmorty.data.repository.paging.PagerFactory
import com.ifucolo.rickandmorty.domain.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Provider

interface EpisodeRepository {
    fun pagedEpisodes(): Flow<PagingData<Episode>>
}

class EpisodeRepositoryImpl @Inject constructor(
    //use provider to always have a fresh instance, because the pager is tied to a single page lifecycle, and resuing it can cause issues, like crashes, race conditions, etc.
    private val episodesRemoteMediator: Provider<EpisodesRemoteMediator>,
    private val local: RickAndMortyLocalDataSource,
    private val pagerFactory: PagerFactory
) : EpisodeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun pagedEpisodes(): Flow<PagingData<Episode>> =
        pagerFactory.create(
            remoteMediator = episodesRemoteMediator.get(),
            pagingSourceFactory = { local.episodesPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
}