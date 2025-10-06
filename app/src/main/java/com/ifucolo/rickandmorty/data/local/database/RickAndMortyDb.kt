package com.ifucolo.rickandmorty.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ifucolo.rickandmorty.data.local.dao.CharacterDao
import com.ifucolo.rickandmorty.data.local.dao.EpisodeDao
import com.ifucolo.rickandmorty.data.local.dao.PageRemoteKeysDao
import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.entity.converter.Converters

@Database(
    entities = [EpisodeEntity::class, CharacterEntity::class, PageRemoteKeysEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RickAndMortyDb : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
    abstract fun characterDao(): CharacterDao
    abstract fun pageRemoteKeysDao(): PageRemoteKeysDao

    companion object {
        fun createDatabase(context: Context): RickAndMortyDb {
            return Room
                .databaseBuilder(context, RickAndMortyDb::class.java, "RickAndMortyDb.db")
                .build()
        }
    }
}