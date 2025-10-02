package com.ifucolo.rickandmorty.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM episodes ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, EpisodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<EpisodeEntity>)

    @Query("DELETE FROM episodes")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM episodes")
    suspend fun count(): Int

    @Transaction
    suspend fun replaceAll(items: List<EpisodeEntity>) {
        clearAll()
        insertAll(items)
    }
}