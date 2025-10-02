package com.ifucolo.rickandmorty.data.local.di

import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSourceImpl
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RickAndMortyLocalModule {

    @Binds
    @Singleton
    abstract fun bindRickAndMortyLocalDataSource(
        rickAndMortyLocalDataSourceImpl: RickAndMortyLocalDataSourceImpl
    ): RickAndMortyLocalDataSource

    @Binds
    @Singleton
    abstract fun bindPageRemoteKeyLocalDataSource(
        pageRemoteKeyLocalDataSourceImpl: PageRemoteKeyLocalDataSourceImpl
    ): PageRemoteKeyLocalDataSource
}