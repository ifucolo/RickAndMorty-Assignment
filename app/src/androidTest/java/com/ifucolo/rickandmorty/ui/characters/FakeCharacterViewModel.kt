package com.ifucolo.rickandmorty.ui.characters

import com.ifucolo.rickandmorty.data.repository.FakeCharacterRepository
import com.ifucolo.rickandmorty.data.repository.characters.CharactersRepository
import com.ifucolo.rickandmorty.ui.feature.characteres.viewmodel.CharacterViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeCharacterViewModel(
    repo: CharactersRepository = FakeCharacterRepository()
) : CharacterViewModel(repo) {
    private val _fakeState = MutableStateFlow<UiState>(UiState.Loading)
    override val state = _fakeState.asStateFlow()

    fun setState(newState: UiState) {
        _fakeState.update { newState }
    }

    override fun load(id: Int) {}
}