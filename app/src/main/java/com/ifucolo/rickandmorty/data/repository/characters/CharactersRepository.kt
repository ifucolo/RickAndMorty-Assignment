package com.ifucolo.rickandmorty.data.repository.characters

import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mapper.toDomain
import com.ifucolo.rickandmorty.data.mapper.toEntity
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import com.ifucolo.rickandmorty.domain.CharacterDetail
import com.ifucolo.rickandmorty.domain.DomainResult
import javax.inject.Inject

interface CharactersRepository {
    suspend fun getCharacter(id: Int): DomainResult<CharacterDetail>
}

class CharactersRepositoryImpl @Inject constructor(
    private val remote: RickAndMortyRemoteDataSource,
    private val local: RickAndMortyLocalDataSource
): CharactersRepository {

    override suspend fun getCharacter(id: Int): DomainResult<CharacterDetail> {
        val characterResult = local.getCharacter(id)
        return when (characterResult) {
            is LocalResult.Success -> {
                DomainResult.Success(characterResult.data.toDomain())
            }

            is LocalResult.Empty -> {
                getRemoteCharacter(id)
            }

            is LocalResult.Error -> {
                DomainResult.Error(characterResult.error)
            }
        }
    }

    private suspend fun getRemoteCharacter(id: Int): DomainResult<CharacterDetail> {
        return when (val result = remote.getCharacter(id)) {
            is ApiResult.Success -> {
                val characterEntity = result.data.toEntity()
                local.upsertCharacter(characterEntity)
                val characterDomain = characterEntity.toDomain()
                DomainResult.Success(characterDomain)
            }
            is ApiResult.Error -> {
                DomainResult.Error(result.error)
            }
        }
    }
}
