package com.ifucolo.rickandmorty.ui.episodes

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ifucolo.rickandmorty.data.datastore.FakeRefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.remote.network.FakeNetworkMonitor
import com.ifucolo.rickandmorty.data.repository.FakeEpisodeRepository
import com.ifucolo.rickandmorty.domain.Episode
import com.ifucolo.rickandmorty.ui.feature.episode.episodes.viewmodel.EpisodesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class FakeEpisodesViewModel(
    val fakeRepository: FakeEpisodeRepository = FakeEpisodeRepository(),
    testScope: CoroutineScope
): EpisodesViewModel(
    repository = fakeRepository,
    refreshPrefsDataSource = FakeRefreshPrefsDataSource(),
    networkMonitor = FakeNetworkMonitor()
) {

    private val _uiEvent = Channel<UiEvent>()
    override val uiEvent = _uiEvent.receiveAsFlow()
    private val _lastRefreshed = MutableStateFlow<String>("")
    override val lastRefreshed = _lastRefreshed.asStateFlow()
    override val episodes: Flow<PagingData<Episode>> =
        fakeRepository.pagedEpisodes().cachedIn(testScope)

    fun setUiEvent(event: UiEvent) {
        _uiEvent.trySend(event)
    }

    fun setLastRefreshed(value: String) {
        _lastRefreshed.update { value }
    }
}