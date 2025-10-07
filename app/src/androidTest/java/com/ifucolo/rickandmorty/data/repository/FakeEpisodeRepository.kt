package com.ifucolo.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ifucolo.rickandmorty.data.repository.episodes.EpisodeRepository
import com.ifucolo.rickandmorty.data.repository.paging.ControllablePagingSource
import com.ifucolo.rickandmorty.domain.Episode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeEpisodeRepository @Inject constructor(): EpisodeRepository {
    val controllablePagingSource = ControllablePagingSource()

    override fun pagedEpisodes(): Flow<PagingData<Episode>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { controllablePagingSource }
        ).flow
    }
}