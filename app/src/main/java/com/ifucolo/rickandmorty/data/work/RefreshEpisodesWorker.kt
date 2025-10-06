package com.ifucolo.rickandmorty.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.repository.refresh.EpisodesRefresher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshEpisodesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val refresher: EpisodesRefresher,
    private val prefs: RefreshPrefsDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        refresher.refresh(pages = 2)
        prefs.setLastRefresh(System.currentTimeMillis())
        Result.success()
    } catch (_: Throwable) { Result.retry() }
}