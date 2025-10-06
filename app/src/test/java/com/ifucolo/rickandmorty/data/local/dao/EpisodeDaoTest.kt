package com.ifucolo.rickandmorty.data.local.dao

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.local.dao.mock.episodeEntity
import com.ifucolo.rickandmorty.data.local.dao.mock.episodesList
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class EpisodeDaoTest {
    private lateinit var db: RickAndMortyDb
    private lateinit var dao: EpisodeDao

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.episodeDao()
    }

    @After
    fun tearDown() { db.close() }


    @Test
    fun insertAll_and_count() = runTest {
        val items = episodesList(1..3)
        dao.insertAll(items)
        val count = dao.count()
        assertEquals(count,3)
    }

    @Test
    fun clearAll_empties_table() = runTest {
        dao.insertAll(episodesList(1..2))
        dao.clearAll()
        assertEquals(dao.count(),0)
    }

    @Test
    fun replaceAll_clears_then_inserts_new_set() = runTest {
        dao.insertAll(episodesList(1..3))
        dao.replaceAll(episodesList(10..12))
        assertEquals(dao.count(),3)

        val ps = dao.pagingSource()
        val result = ps.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 50,
                placeholdersEnabled = false
            )
        )
        require(result is PagingSource.LoadResult.Page)
        val ids = result.data.map { it.id }
        assertEquals(listOf(10, 11, 12), ids)
    }

    @Test
    fun pagingSource_orders_by_id_asc() = runTest {
        dao.insertAll(listOf(
            episodeEntity(3), episodeEntity(1), episodeEntity(2)
        ))

        val ps = dao.pagingSource()
        val result = ps.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 50,
                placeholdersEnabled = false
            )
        )

        require(result is PagingSource.LoadResult.Page)
        val ids = result.data.map { it.id }
        assertEquals(listOf(1, 2, 3), ids)
    }
}