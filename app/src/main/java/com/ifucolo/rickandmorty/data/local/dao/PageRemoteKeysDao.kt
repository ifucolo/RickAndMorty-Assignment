package com.ifucolo.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity

@Dao
interface PageRemoteKeysDao {
    @Query("SELECT * FROM page_remote_keys WHERE label = :label LIMIT 1")
    suspend fun getRemoteKeys(label: String): PageRemoteKeysEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(keys: PageRemoteKeysEntity)

    @Query("DELETE FROM page_remote_keys WHERE label = :label")
    suspend fun clear(label: String)
}