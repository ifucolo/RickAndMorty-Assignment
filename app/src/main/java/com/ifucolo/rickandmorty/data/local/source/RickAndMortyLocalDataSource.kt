package com.ifucolo.rickandmorty.data.local.source

import androidx.paging.PagingSource
import com.ifucolo.rickandmorty.data.local.dao.CharacterDao
import com.ifucolo.rickandmorty.data.local.dao.EpisodeDao
import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import javax.inject.Inject

interface RickAndMortyLocalDataSource {
    // Episode operations
    fun episodesPagingSource(): PagingSource<Int, EpisodeEntity>
    suspend fun insertEpisodes(items: List<EpisodeEntity>)
    suspend fun clearEpisodes()
    suspend fun replaceAllEpisodes(items: List<EpisodeEntity>)
    suspend fun episodesCount(): Int

    // Character operations
    suspend fun getCharacter(id: Int): CharacterEntity?
    suspend fun upsertCharacter(entity: CharacterEntity)
    suspend fun upsertCharacters(items: List<CharacterEntity>)
}

class RickAndMortyLocalDataSourceImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val episodeDao: EpisodeDao
): RickAndMortyLocalDataSource {

    // Episode operations
    override fun episodesPagingSource(): PagingSource<Int, EpisodeEntity> =
        episodeDao.pagingSource()

    override suspend fun insertEpisodes(items: List<EpisodeEntity>) {
        episodeDao.insertAll(items)
    }

    override suspend fun clearEpisodes() {
        episodeDao.clearAll()
    }

    override suspend fun replaceAllEpisodes(items: List<EpisodeEntity>) {
        episodeDao.replaceAll(items)
    }

    override suspend fun episodesCount(): Int = episodeDao.count()


    // Character operations
    override suspend fun getCharacter(id: Int): CharacterEntity? =
        characterDao.getById(id)

    override suspend fun upsertCharacter(entity: CharacterEntity) {
        characterDao.upsert(entity)
    }

    override suspend fun upsertCharacters(items: List<CharacterEntity>) {
        characterDao.upsertAll(items)
    }
}