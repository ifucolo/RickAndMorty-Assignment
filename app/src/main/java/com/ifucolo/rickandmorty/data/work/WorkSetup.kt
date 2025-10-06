package com.ifucolo.rickandmorty.data.work

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val WORK_NAME_REFRESH_EPISODES = "refresh_episodes_periodic"

fun enqueueEpisodesPeriodicRefresh(workManager: WorkManager) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    val request = PeriodicWorkRequestBuilder<RefreshEpisodesWorker>(
        repeatInterval = 15,
        repeatIntervalTimeUnit = TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .build()

    workManager.enqueueUniquePeriodicWork(
        WORK_NAME_REFRESH_EPISODES,
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}