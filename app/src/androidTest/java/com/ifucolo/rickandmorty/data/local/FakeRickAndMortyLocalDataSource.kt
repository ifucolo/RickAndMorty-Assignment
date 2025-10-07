package com.ifucolo.rickandmorty.data.local

import androidx.paging.PagingSource
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.dto.OperationResult
import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import javax.inject.Inject

class FakeRickAndMortyLocalDataSource @Inject constructor(): RickAndMortyLocalDataSource {
    override fun episodesPagingSource(): PagingSource<Int, EpisodeEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertEpisodes(items: List<EpisodeEntity>): OperationResult {
        TODO("Not yet implemented")
    }

    override suspend fun clearEpisodes(): OperationResult {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): LocalResult<CharacterEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertCharacter(entity: CharacterEntity): OperationResult {
        TODO("Not yet implemented")
    }
}