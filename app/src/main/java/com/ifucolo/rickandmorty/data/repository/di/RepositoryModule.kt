package com.ifucolo.rickandmorty.data.repository.di

import com.ifucolo.rickandmorty.data.repository.EpisodeRepository
import com.ifucolo.rickandmorty.data.repository.EpisodeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEpisodeRepository(
        episodeRepositoryImpl: EpisodeRepositoryImpl
    ): EpisodeRepository

}