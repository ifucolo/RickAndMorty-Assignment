package com.ifucolo.rickandmorty.data.datastore


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.ds by preferencesDataStore("sync_prefs")
private val LAST_REFRESH_EPOCH = longPreferencesKey("last_refresh_epoch")

interface RefreshPrefsDataSource {
    fun lastRefreshFlow(): Flow<Long>
    suspend fun setLastRefresh(epochMillis: Long)
}

class RefreshPrefsDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
): RefreshPrefsDataSource {
    override fun lastRefreshFlow(): Flow<Long> =
        appContext.ds.data.map { it[LAST_REFRESH_EPOCH] ?: 0L }

    override suspend fun setLastRefresh(epochMillis: Long) {
        appContext.ds.edit { it[LAST_REFRESH_EPOCH] = epochMillis }
    }
}
