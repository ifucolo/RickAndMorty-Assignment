package com.ifucolo.rickandmorty.data.repository.di

import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitor
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import com.ifucolo.rickandmorty.data.repository.paging.EpisodesRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PagingModule {
    @Provides
    fun provideEpisodesRemoteMediator(
        db: RickAndMortyDb,
        remote: RickAndMortyRemoteDataSource,
        pageKeysLocal: PageRemoteKeyLocalDataSource,
        local: RickAndMortyLocalDataSource,
        refreshPrefsDataSource: RefreshPrefsDataSource,
        networkMonitor: NetworkMonitor
    ): EpisodesRemoteMediator = EpisodesRemoteMediator(
        db = db,
        rickAndMortyRemoteDataSource = remote,
        pageRemoteKeyLocalDataSource = pageKeysLocal,
        rickAndMortyLocalDataSource = local,
        refreshPrefsDataSource = refreshPrefsDataSource,
        networkMonitor = networkMonitor
    )

}