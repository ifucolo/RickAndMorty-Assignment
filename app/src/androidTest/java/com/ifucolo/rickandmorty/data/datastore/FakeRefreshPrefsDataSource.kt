package com.ifucolo.rickandmorty.data.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeRefreshPrefsDataSource @Inject constructor(): RefreshPrefsDataSource {
    private val lastRefreshTimestamp = MutableStateFlow(0L)

    override fun lastRefreshFlow(): Flow<Long> {
        return lastRefreshTimestamp
    }

    override suspend fun setLastRefresh(epochMillis: Long) {
        lastRefreshTimestamp.update { epochMillis }
    }

    fun getCurrentValue(): Long {
        return lastRefreshTimestamp.value
    }
}