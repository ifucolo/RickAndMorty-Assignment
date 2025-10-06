package com.ifucolo.rickandmorty.data.datastore.di

import android.content.Context
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    fun provideExampleDependency(
        @ApplicationContext
        context: Context
    ): RefreshPrefsDataSource {
        return RefreshPrefsDataSourceImpl(appContext = context)
    }
}