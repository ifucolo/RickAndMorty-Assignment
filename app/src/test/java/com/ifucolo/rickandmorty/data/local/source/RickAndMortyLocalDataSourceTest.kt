package com.ifucolo.rickandmorty.data.local.source

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.local.dao.CharacterDao
import com.ifucolo.rickandmorty.data.local.dao.EpisodeDao
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.dto.OperationResult
import com.ifucolo.rickandmorty.data.local.entity.CharacterEntity
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.mock.characterEntity
import com.ifucolo.rickandmorty.data.local.mock.episodeEntity
import com.ifucolo.rickandmorty.data.local.mock.episodesList
import com.ifucolo.rickandmorty.data.local.source.fake.ThrowingCharacterDao
import com.ifucolo.rickandmorty.data.local.source.fake.ThrowingEpisodeDao
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
class RickAndMortyLocalDataSourceTest {
    private lateinit var db: RickAndMortyDb
    private lateinit var episodeDao: EpisodeDao
    private lateinit var characterDao: CharacterDao
    private lateinit var source: RickAndMortyLocalDataSource

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
            .allowMainThreadQueries()
            .build()
        episodeDao = db.episodeDao()
        characterDao = db.characterDao()

        source = RickAndMortyLocalDataSourceImpl(episodeDao = episodeDao, characterDao = characterDao)
    }

    @After
    fun teardown() {
        db.close()
    }


    @Test
    fun insertEpisodes_success_returnsSuccess_andPersists() = runTest {
        val items = episodesList(1..3)

        val res = source.insertEpisodes(items)
        assertTrue(res is OperationResult.Success)

        val count = episodeDao.count()
        assertEquals(3, count)

        val ps = episodeDao.pagingSource()
        val page = ps.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 50,
                placeholdersEnabled = false
            )
        )
        require(page is PagingSource.LoadResult.Page)
        val ids = page.data.map { it.id }
        assertEquals(listOf(1, 2, 3), ids)
    }

    @Test
    fun clearEpisodes_success_returnsSuccess_andClearsTable() = runTest {
        episodeDao.insertAll(episodesList(1..1))
        assertEquals(1, episodeDao.count())

        val res = source.clearEpisodes()
        assertTrue(res is OperationResult.Success)
        assertEquals(0, episodeDao.count())
    }


    @Test
    fun getCharacter_found_returnsSuccess() = runTest {
        val rick = characterEntity()
        characterDao.upsert(rick)

        val res = source.getCharacter(1)
        assertTrue(res is LocalResult.Success)
        val data = (res as LocalResult.Success).data
        assertEquals(1, data.id)
        assertEquals("Rick", data.name)
    }

    @Test
    fun getCharacter_missing_returnsEmpty() = runTest {
        val res = source.getCharacter(999)
        assertTrue(res is LocalResult.Empty)
    }

    @Test
    fun upsertCharacter_success_returnsSuccess_andPersists() = runTest {
        val morty = characterEntity(2, "Morty")
        val res = source.upsertCharacter(morty)
        assertTrue(res is OperationResult.Success)

        val loaded = characterDao.getById(2)
        assertNotNull(loaded)
        assertEquals("Morty", loaded.name)
    }

    @Test
    fun insertEpisodes_daoThrows_returnsError() = runTest {
        val throwing = ThrowingEpisodeDao()
        val src = RickAndMortyLocalDataSourceImpl(
            characterDao = characterDao,
            episodeDao = throwing
        )

        val res = src.insertEpisodes(episodesList(1..1))
        assertTrue(res is OperationResult.Error)
        assertEquals("boom-insert", (res as OperationResult.Error).error.message)
    }

    @Test
    fun clearEpisodes_daoThrows_returnsError() = runTest {
        val throwing = ThrowingEpisodeDao()
        val src = RickAndMortyLocalDataSourceImpl(
            characterDao = characterDao,
            episodeDao = throwing
        )

        val res = src.clearEpisodes()
        assertTrue(res is OperationResult.Error)
       assertEquals("boom-clear", (res as OperationResult.Error).error.message)
    }

    @Test
    fun getCharacter_daoThrows_returnsError() = runTest {
        val throwing = ThrowingCharacterDao()
        val src = RickAndMortyLocalDataSourceImpl(
            characterDao = throwing,
            episodeDao = episodeDao
        )

        val res = src.getCharacter(1)
        assertTrue(res is LocalResult.Error)
        assertEquals("boom-get", (res as LocalResult.Error).error.message)
    }

    @Test
    fun upsertCharacter_daoThrows_returnsError() = runTest {
        val throwing = ThrowingCharacterDao()
        val src = RickAndMortyLocalDataSourceImpl(
            characterDao = throwing,
            episodeDao = episodeDao
        )

        val res = src.upsertCharacter(characterEntity())
        assertTrue(res is OperationResult.Error)
        assertEquals("boom-upsert", (res as OperationResult.Error).error.message)
    }
}