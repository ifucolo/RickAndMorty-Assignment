package com.ifucolo.rickandmorty.data.repository.refresh

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeys
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.dto.EpisodeDto
import com.ifucolo.rickandmorty.data.remote.dto.EpisodePageDto
import com.ifucolo.rickandmorty.data.remote.dto.InfoDto
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class EpisodesRefresherTest {
    private lateinit var db: RickAndMortyDb
    private lateinit var remote: RickAndMortyRemoteDataSource
    private lateinit var local: RickAndMortyLocalDataSource
    private lateinit var pageKeys: PageRemoteKeyLocalDataSource
    private lateinit var refresher: EpisodesRefresher

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
            .allowMainThreadQueries()
            .build()
        remote = Mockito.mock(RickAndMortyRemoteDataSource::class.java)
        local = Mockito.mock(RickAndMortyLocalDataSource::class.java)
        pageKeys = Mockito.mock(PageRemoteKeyLocalDataSource::class.java)
        refresher = EpisodesRefresherImpl(db, remote, local, pageKeys)
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun `refresh successfully clears old data, fetches two pages, and inserts new data`() = runTest {
        val page1Result = createFakePageResult(currentPage = 1, nextPage = 2)
        val page2Result = createFakePageResult(currentPage = 2, nextPage = null) // Last page

        whenever(remote.getEpisodes(1)).thenReturn(ApiResult.Success(page1Result))
        whenever(remote.getEpisodes(2)).thenReturn(ApiResult.Success(page2Result))

        refresher.refresh(pages = 2)

        val inOrder = inOrder(local, pageKeys, remote)

        inOrder.verify(local).clearEpisodes()
        inOrder.verify(pageKeys).clear(PageRemoteKeys.EPISODES_FEED_KEY.key)

        inOrder.verify(remote).getEpisodes(1)
        inOrder.verify(local).insertEpisodes(any())
        inOrder.verify(pageKeys).upsert(any())

        inOrder.verify(remote).getEpisodes(2)
        inOrder.verify(local).insertEpisodes(any())
        inOrder.verify(pageKeys).upsert(any())

        verify(remote, times(2)).getEpisodes(any())
        verify(local, times(2)).insertEpisodes(any())
        verify(pageKeys, times(2)).upsert(any())
    }

    @Test
    fun `refresh stops fetching when a page has no next page`() = runTest {
        val page1Result = createFakePageResult(currentPage = 1, nextPage = null) // No next page

        whenever(remote.getEpisodes(1)).thenReturn(ApiResult.Success(page1Result))

        refresher.refresh(pages = 3)

        verify(remote).getEpisodes(1)
        verify(local).insertEpisodes(any())
        verify(pageKeys).upsert(any())

        verify(remote, times(1)).getEpisodes(any())
    }

    @Test(expected = IllegalStateException::class)
    fun `refresh throws exception when remote call fails`() = runTest {
        val error = IllegalStateException("Network failed")
        whenever(remote.getEpisodes(1)).thenReturn(ApiResult.Error(error))

        refresher.refresh(pages = 1)
        verify(remote, times(21)).getEpisodes(any())
        verify(local, times(0)).insertEpisodes(any())
        verify(pageKeys, times(1555)).upsert(any())
    }
}

private fun createFakePageResult(currentPage: Int, nextPage: Int?): EpisodePageDto {
    return EpisodePageDto(
        info = InfoDto(
            count = 40,
            pages = 2,
            next = nextPage?.let { "https://fakeapi.com/episode?page=$it" },
            prev = null
        ),
        results = listOf(
            EpisodeDto(
                id = currentPage * 10,
                name = "Episode ${currentPage * 10}",
                airDate = "Date",
                episode = "S01E${currentPage * 10}",
                characters = emptyList()
            )
        )
    )
}