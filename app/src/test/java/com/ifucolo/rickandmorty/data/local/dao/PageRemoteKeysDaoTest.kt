package com.ifucolo.rickandmorty.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.local.dao.mock.pageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class PageRemoteKeysDaoTest {

    private lateinit var db: RickAndMortyDb
    private lateinit var dao: PageRemoteKeysDao

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.pageRemoteKeysDao()
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun upsert_then_get_returns_row() = runTest {
        val now = 100L
        val row = pageRemoteKeysEntity

        dao.upsert(row)
        val loaded = dao.getRemoteKeys("episodes")

        assertNotNull(loaded)
        assertEquals(loaded.label,"episodes")
        assertEquals(loaded.nextPage, 2)
        assertEquals(loaded.lastUpdated, now)
    }

    @Test
    fun upsert_overwrites_existing_row_for_same_label() = runTest {
        dao.upsert(pageRemoteKeysEntity)
        dao.upsert(pageRemoteKeysEntity.copy(nextPage = 3, lastUpdated = 200L))

        val loaded = dao.getRemoteKeys("episodes")

        assertNotNull(loaded)
        assertEquals(loaded.nextPage,3)
        assertEquals(loaded.lastUpdated,200L)
    }

    @Test
    fun multiple_labels_coexist_independently() = runTest {
        dao.upsert(pageRemoteKeysEntity)
        dao.upsert(pageRemoteKeysEntity.copy(label = "characters", nextPage = 5, lastUpdated = 150L))

        val episodes = dao.getRemoteKeys("episodes")
        val characters = dao.getRemoteKeys("characters")

        assertNotNull(episodes)
        assertNotNull(characters)
        assertEquals(episodes.nextPage, 2)
        assertEquals(characters.nextPage, 5)
    }

    @Test
    fun clear_removes_only_that_label() = runTest {
        dao.upsert(pageRemoteKeysEntity)
        dao.upsert(pageRemoteKeysEntity.copy(label = "characters", nextPage = 5, lastUpdated = 150L))

        dao.clear("episodes")

        val episodes = dao.getRemoteKeys("episodes")
        val characters = dao.getRemoteKeys("characters")

        assertNull(episodes)
        assertNotNull(characters)

        assertEquals(characters.nextPage, 5)
    }

    @Test
    fun get_returns_null_when_missing() = runTest {
        val loaded = dao.getRemoteKeys("not_there")
        assertNull(loaded)
    }
}