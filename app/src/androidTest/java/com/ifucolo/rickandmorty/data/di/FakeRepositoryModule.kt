package com.ifucolo.rickandmorty.data.di

import com.ifucolo.rickandmorty.data.repository.FakeCharacterRepository
import com.ifucolo.rickandmorty.data.repository.FakeEpisodeRepository
import com.ifucolo.rickandmorty.data.repository.FakeEpisodesRefresher
import com.ifucolo.rickandmorty.data.repository.FakePagerFactory
import com.ifucolo.rickandmorty.data.repository.characters.CharactersRepository
import com.ifucolo.rickandmorty.data.repository.di.RepositoryModule
import com.ifucolo.rickandmorty.data.repository.episodes.EpisodeRepository
import com.ifucolo.rickandmorty.data.repository.paging.PagerFactory
import com.ifucolo.rickandmorty.data.repository.refresh.EpisodesRefresher
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindFakeCharacterRepository(
        fakeRepository: FakeCharacterRepository
    ): CharactersRepository

    @Singleton
    @Binds
    abstract fun bindFakeEpisodesRefresher(
        fakeRefresher: FakeEpisodesRefresher
    ): EpisodesRefresher


    @Singleton
    @Binds
    abstract fun bindFakeEpisodeRepository(
        fakeEpisodeRepository: FakeEpisodeRepository
    ): EpisodeRepository


    @Singleton
    @Binds
    abstract fun bindFakeFakePagerFactory(
        fakePagerFactory: FakePagerFactory
    ): PagerFactory
}