package com.ifucolo.rickandmorty.data.repository.di

import com.ifucolo.rickandmorty.data.repository.characters.CharactersRepository
import com.ifucolo.rickandmorty.data.repository.characters.CharactersRepositoryImpl
import com.ifucolo.rickandmorty.data.repository.episodes.EpisodeRepository
import com.ifucolo.rickandmorty.data.repository.episodes.EpisodeRepositoryImpl
import com.ifucolo.rickandmorty.data.repository.refresh.EpisodesRefresher
import com.ifucolo.rickandmorty.data.repository.refresh.EpisodesRefresherImpl
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

    @Binds
    @Singleton
    abstract fun bindCharactersRepository(
        charactersRepositoryImpl: CharactersRepositoryImpl
    ): CharactersRepository

    @Binds
    @Singleton
    abstract fun bindEpisodesRefresher(
        episodesRefresherImpl: EpisodesRefresherImpl
    ): EpisodesRefresher
}