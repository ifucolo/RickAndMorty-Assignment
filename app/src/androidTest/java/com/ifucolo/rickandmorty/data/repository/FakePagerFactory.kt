package com.ifucolo.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.repository.paging.PagerFactory
import javax.inject.Inject

class FakePagerFactory @Inject constructor(): PagerFactory {
    @OptIn(ExperimentalPagingApi::class)
    override fun create(
        config: PagingConfig,
        remoteMediator: RemoteMediator<Int, EpisodeEntity>,
        pagingSourceFactory: () -> PagingSource<Int, EpisodeEntity>
    ): Pager<Int, EpisodeEntity> {
        return Pager(
            config = config,
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        )
    }
}