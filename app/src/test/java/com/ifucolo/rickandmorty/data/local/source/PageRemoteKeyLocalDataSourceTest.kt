package com.ifucolo.rickandmorty.data.local.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.local.dao.PageRemoteKeysDao
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.dto.OperationResult
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.mock.pageRemoteKeysEntity
import junit.framework.TestCase.assertTrue
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
class PageRemoteKeyLocalDataSourceTest {
    private lateinit var db: RickAndMortyDb
    private lateinit var realDao: PageRemoteKeysDao
    private lateinit var source: PageRemoteKeyLocalDataSource

    //this is used to simlate DAO failures
    private class ThrowingPageRemoteKeysDao : PageRemoteKeysDao {
        override suspend fun getRemoteKeys(label: String): PageRemoteKeysEntity? {
            throw RuntimeException("boom-get")
        }
        override suspend fun upsert(keys: PageRemoteKeysEntity) {
            throw RuntimeException("boom-upsert")
        }
        override suspend fun clear(label: String) {
            throw RuntimeException("boom-clear")
        }
    }

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
            .allowMainThreadQueries()
            .build()
        realDao = db.pageRemoteKeysDao()
        source = PageRemoteKeyLocalDataSourceImpl(realDao)
    }

    @After
    fun teardown() {
        db.close()
    }


    @Test
    fun getRemoteKeys_success_returnsSuccess() = runTest {
        val row = pageRemoteKeysEntity
        realDao.upsert(row)

        val res = source.getRemoteKeys("episodes")

        assertTrue(res is LocalResult.Success)
        val data = (res as LocalResult.Success).data
        assertEquals("episodes", data.label)
        assertEquals(2, data.nextPage)
        assertEquals(100L, data.lastUpdated)
    }

    @Test
    fun getRemoteKeys_missing_returnsEmpty() = runTest {
        val res = source.getRemoteKeys("not_there")
        assertTrue(res is LocalResult.Empty)
    }

    @Test
    fun getRemoteKeys_daoThrows_returnsError() = runTest {
        val throwingSource = PageRemoteKeyLocalDataSourceImpl(ThrowingPageRemoteKeysDao())
        val res = throwingSource.getRemoteKeys("episodes")
        assertTrue(res is LocalResult.Error)
        val err = (res as LocalResult.Error).error
        assertNotNull(err)
        assertEquals("boom-get", err.message)
    }

    @Test
    fun upsert_success_returnsSuccess_andPersists() = runTest {
        val res = source.upsert(pageRemoteKeysEntity.copy(nextPage = 3, lastUpdated = 200L))
        assertTrue(res is OperationResult.Success)

        val loaded = realDao.getRemoteKeys("episodes")
        assertNotNull(loaded)
        assertEquals(3, loaded!!.nextPage)
        assertEquals(200L, loaded.lastUpdated)
    }

    @Test
    fun upsert_daoThrows_returnsError() = runTest {
        val throwingSource = PageRemoteKeyLocalDataSourceImpl(ThrowingPageRemoteKeysDao())
        val res = throwingSource.upsert(pageRemoteKeysEntity.copy(nextPage = 1, lastUpdated = 1L))
        assertTrue(res is OperationResult.Error)
        val err = (res as OperationResult.Error).error
        assertNotNull(err)
        assertEquals("boom-upsert", err.message)
    }

    @Test
    fun clear_success_returnsSuccess_andOnlyRemovesThatLabel() = runTest {
        realDao.upsert(pageRemoteKeysEntity)
        realDao.upsert(pageRemoteKeysEntity.copy("characters",nextPage = 5, lastUpdated = 150L))

        val res = source.clear("episodes")
        assertTrue(res is OperationResult.Success)

        val ep = realDao.getRemoteKeys("episodes")
        val ch = realDao.getRemoteKeys("characters")

        assertNull(ep)
        assertNotNull(ch)
        assertEquals(5, ch.nextPage)
        assertEquals(150L, ch.lastUpdated)
    }

    @Test
    fun clear_daoThrows_returnsError() = runTest {
        val throwingSource = PageRemoteKeyLocalDataSourceImpl(ThrowingPageRemoteKeysDao())
        val res = throwingSource.clear("episodes")
        assertTrue(res is OperationResult.Error)
        val err = (res as OperationResult.Error).error
        assertNotNull(err)
        assertEquals("boom-clear", err.message)
    }
}