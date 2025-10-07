package com.ifucolo.rickandmorty.data.repository

import com.ifucolo.rickandmorty.data.repository.characters.CharactersRepository
import com.ifucolo.rickandmorty.domain.CharacterDetail
import com.ifucolo.rickandmorty.domain.DomainResult
import java.io.IOException
import javax.inject.Inject

class FakeCharacterRepository @Inject constructor(): CharactersRepository {

    private var characterDetail: CharacterDetail? = null
    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setCharacterDetail(character: CharacterDetail) {
        this.characterDetail = character
        shouldReturnError = false
    }

    override suspend fun getCharacter(id: Int): DomainResult<CharacterDetail> {
        if (shouldReturnError) {
            DomainResult.Error(IOException("Test exception"))
        }

        return characterDetail?.let {
            DomainResult.Success(it)
        } ?: throw IllegalStateException("No character detail was set for the test.")
    }
}