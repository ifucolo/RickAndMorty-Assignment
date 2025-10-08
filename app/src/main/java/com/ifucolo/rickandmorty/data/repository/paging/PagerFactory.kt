package com.ifucolo.rickandmorty.data.repository.paging
import androidx.paging.*
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import javax.inject.Inject

/**
 * A factory to abstract away the Pager constructor, making the repository testable.
 */
interface PagerFactory {
    @OptIn(ExperimentalPagingApi::class)
    fun create(
        config: PagingConfig = PagingConfig(
            pageSize = 20,
            initialLoadSize = 60,
            prefetchDistance = 10,
            enablePlaceholders = true,
            jumpThreshold = 20 * 3,
            maxSize = 200
        ),
        remoteMediator: RemoteMediator<Int, EpisodeEntity>,
        pagingSourceFactory: () -> PagingSource<Int, EpisodeEntity>
    ): Pager<Int, EpisodeEntity>
}

class PagerFactoryImpl @Inject constructor() : PagerFactory {
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