package com.ifucolo.rickandmorty.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.local.dao.mock.characterEntity
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
class CharacterDaoTest {
    private lateinit var db: RickAndMortyDb
    private lateinit var dao: CharacterDao

    @Before
    fun setup() {
         val ctx = ApplicationProvider.getApplicationContext<Context>()
         db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
             .allowMainThreadQueries()
             .build()
         dao = db.characterDao()
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun insert_character() = runTest {
        dao.upsert(characterEntity)
        val loaded = dao.getById(1)
        assertNotNull(loaded)
        assert(loaded.name == "Rick")
    }

    @Test
    fun insert_characters() = runTest {
        val list = listOf(
            characterEntity,
            characterEntity.copy(id = 2, name = "Morty"),
            characterEntity.copy(id = 3, name = "Summer"),
        )
        dao.upsertAll(list)
        val loaded = dao.getById(2)
        assertNotNull(loaded)
        assert(loaded.name == "Morty")
    }
}