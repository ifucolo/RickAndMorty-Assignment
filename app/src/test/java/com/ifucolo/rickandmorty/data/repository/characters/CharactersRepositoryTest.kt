package com.ifucolo.rickandmorty.data.repository.characters

import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mapper.toEntity
import com.ifucolo.rickandmorty.data.mock.characterDto
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import com.ifucolo.rickandmorty.domain.DomainResult
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class CharactersRepositoryTest {

    private lateinit var charactersRepository: CharactersRepository
    private lateinit var remote: RickAndMortyRemoteDataSource
    private lateinit var local: RickAndMortyLocalDataSource

    @Before
    fun setUp() {
        remote = Mockito.mock(RickAndMortyRemoteDataSource::class.java)
        local = Mockito.mock(RickAndMortyLocalDataSource::class.java)
        charactersRepository = CharactersRepositoryImpl(remote, local)
    }

    @Test
    fun test_getCharacter_from_remote() = runTest {
        `when`(local.getCharacter(1)).thenReturn(LocalResult.Empty)
        `when`(remote.getCharacter(1)).thenReturn(ApiResult.Success(data = characterDto()))
        val result = charactersRepository.getCharacter(1)
        assert(result is DomainResult.Success)
        val character = (result as DomainResult.Success).data
        assert(character.id == 1)
        assert(character.name == "Rick Sanchez")
        assert(character.status == "Alive")
        assert(character.species == "Human")
        assert(character.originName == "Earth (C-137)")
    }


    @Test
    fun test_getCharacter_from_remote_error_and_no_local() = runTest {
        `when`(local.getCharacter(1)).thenReturn(LocalResult.Empty)
        `when`(remote.getCharacter(1)).thenReturn(ApiResult.Error(Exception("Network error")))
        val result = charactersRepository.getCharacter(1)
        assert(result is DomainResult.Error)
        val error = (result as DomainResult.Error).error
        assert(error.message == "Network error")
    }

    @Test
    fun test_getCharacter_from_local() = runTest {
        `when`(local.getCharacter(1)).thenReturn(LocalResult.Success(characterDto().toEntity()))
        val result = charactersRepository.getCharacter(1)
        assert(result is DomainResult.Success)
        val character = (result as DomainResult.Success).data
        assert(character.id == 1)
        assert(character.name == "Rick Sanchez")
        assert(character.status == "Alive")
        assert(character.species == "Human")
        assert(character.originName == "Earth (C-137)")
    }
}