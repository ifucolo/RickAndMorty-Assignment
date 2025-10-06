package com.ifucolo.rickandmorty

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.ifucolo.rickandmorty.data.work.enqueueEpisodesPeriodicRefresh
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent


@HiltAndroidApp
class RickAndMortyApp: Application(), Configuration.Provider {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override fun onCreate() {
        super.onCreate()
        enqueueEpisodesPeriodicRefresh(WorkManager.getInstance(this))
    }

    override val workManagerConfiguration: Configuration = Configuration.Builder()
            .setWorkerFactory(
                EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
            )
            .build()
}