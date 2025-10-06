package com.ifucolo.rickandmorty.ui.feature.characteres.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifucolo.rickandmorty.data.repository.characters.CharactersRepository
import com.ifucolo.rickandmorty.domain.CharacterDetail
import com.ifucolo.rickandmorty.domain.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharactersRepository
): ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state

    fun load(id: Int) = viewModelScope.launch {
        _state.value = UiState.Loading
        runCatching { repository.getCharacter(id) }
            .onSuccess { domainResult ->
                when(domainResult) {
                    DomainResult.Empty -> {
                        _state.update { UiState.Error(Throwable("Character not found")) }
                    }
                    is DomainResult.Error -> {
                        _state.update { UiState.Error(domainResult.error) }
                    }
                    DomainResult.Loading -> {
                        _state.update { UiState.Loading }
                    }
                    is DomainResult.Success<CharacterDetail> -> {
                        _state.update { UiState.Data(domainResult.data) }
                    }
                }

            }
            .onFailure { _state.value = UiState.Error(it) }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Data(val character: CharacterDetail) : UiState
        data class Error(val error: Throwable) : UiState
    }
}