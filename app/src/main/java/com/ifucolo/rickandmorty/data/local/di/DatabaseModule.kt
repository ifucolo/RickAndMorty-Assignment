package com.ifucolo.rickandmorty.data.local.di

import android.content.Context
import com.ifucolo.rickandmorty.data.local.dao.CharacterDao
import com.ifucolo.rickandmorty.data.local.dao.EpisodeDao
import com.ifucolo.rickandmorty.data.local.dao.PageRemoteKeysDao
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): RickAndMortyDb = RickAndMortyDb.createDatabase(context = context)

    @Provides fun provideEpisodeDao(db: RickAndMortyDb): EpisodeDao = db.episodeDao()
    @Provides fun provideCharacterDao(db: RickAndMortyDb): CharacterDao = db.characterDao()
    @Provides fun providePageRemoteKeysDao(db: RickAndMortyDb): PageRemoteKeysDao = db.pageRemoteKeysDao()
}