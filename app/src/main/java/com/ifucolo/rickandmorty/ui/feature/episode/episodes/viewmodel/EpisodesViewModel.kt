package com.ifucolo.rickandmorty.ui.feature.episode.episodes.viewmodel

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitor
import com.ifucolo.rickandmorty.data.repository.episodes.EpisodeRepository
import com.ifucolo.rickandmorty.domain.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    repository: EpisodeRepository,
    refreshPrefsDataSource: RefreshPrefsDataSource,
    private val networkMonitor: NetworkMonitor
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val episodes: Flow<PagingData<Episode>> =
        repository.pagedEpisodes().cachedIn(viewModelScope)

    val lastRefreshed: StateFlow<String> =
        refreshPrefsDataSource.lastRefreshFlow()
            .map { epoch ->
                if (epoch == 0L) "-"
                else {
                    val df = DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault()
                    )
                    df.format(Date(epoch))
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "—")



    fun onPullToRefresh(refreshPaging: () -> Unit) {
        viewModelScope.launch {
            if (networkMonitor.isConnected()) {
                refreshPaging()
            } else {
                _uiEvent.send(UiEvent.NoInternet)
            }
        }
    }

    sealed interface UiEvent {
        data object NoInternet: UiEvent
    }
}