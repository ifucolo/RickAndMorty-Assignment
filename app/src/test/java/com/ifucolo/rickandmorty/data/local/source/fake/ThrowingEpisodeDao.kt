package com.ifucolo.rickandmorty.data.local.source.fake

import androidx.paging.PagingSource
import com.ifucolo.rickandmorty.data.local.dao.EpisodeDao
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity

class ThrowingEpisodeDao : EpisodeDao {
        override fun pagingSource(): PagingSource<Int, EpisodeEntity> =
            throw UnsupportedOperationException("not used here")
        override suspend fun insertAll(items: List<EpisodeEntity>) {
            throw RuntimeException("boom-insert")
        }
        override suspend fun clearAll(): Unit {
            throw RuntimeException("boom-clear")
        }
        override suspend fun count(): Int = 0
        override suspend fun replaceAll(items: List<EpisodeEntity>) = Unit
    }