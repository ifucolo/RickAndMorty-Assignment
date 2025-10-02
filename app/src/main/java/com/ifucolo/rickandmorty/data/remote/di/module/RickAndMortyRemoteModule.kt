package com.ifucolo.rickandmorty.data.remote.di.module

import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RickAndMortyRemoteModule {

    @Binds
    @Singleton
    abstract fun bindRickAndMortyRemoteDataSource(
        orderRemoteDataSourceImpl: RickAndMortyRemoteDataSourceImpl
    ): RickAndMortyRemoteDataSource
}