package com.ifucolo.rickandmorty.data.repository.paging

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.local.database.RickAndMortyDb
import com.ifucolo.rickandmorty.data.local.dto.LocalResult
import com.ifucolo.rickandmorty.data.local.entity.PageRemoteKeysEntity
import com.ifucolo.rickandmorty.data.local.source.PageRemoteKeyLocalDataSource
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.mock.episodeDto
import com.ifucolo.rickandmorty.data.remote.dto.ApiResult
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitor
import com.ifucolo.rickandmorty.data.remote.source.RickAndMortyRemoteDataSource
import com.ifucolo.rickandmorty.data.repository.mock.emptyPagingState
import com.ifucolo.rickandmorty.data.repository.mock.pageDto
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.kotlin.check
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals



@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
class EpisodesRemoteMediatorTest{

    private lateinit var db: RickAndMortyDb
    private lateinit var rickAndMortyRemoteDataSource: RickAndMortyRemoteDataSource
    private lateinit var pageRemoteKeyLocalDataSource: PageRemoteKeyLocalDataSource
    private lateinit var rickAndMortyLocalDataSource: RickAndMortyLocalDataSource
    private lateinit var refreshPrefsDataSource: RefreshPrefsDataSource
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var episodesRemoteMediator: EpisodesRemoteMediator


    @Before
    fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, RickAndMortyDb::class.java)
            .allowMainThreadQueries()
            .build()

        rickAndMortyRemoteDataSource =  Mockito.mock(RickAndMortyRemoteDataSource::class.java)
        pageRemoteKeyLocalDataSource =  Mockito.mock(PageRemoteKeyLocalDataSource::class.java)
        rickAndMortyLocalDataSource =  Mockito.mock(RickAndMortyLocalDataSource::class.java)
        refreshPrefsDataSource =  Mockito.mock(RefreshPrefsDataSource::class.java)
        networkMonitor =  Mockito.mock(NetworkMonitor::class.java)
        episodesRemoteMediator = EpisodesRemoteMediator(
            db,
            rickAndMortyRemoteDataSource,
            pageRemoteKeyLocalDataSource,
            rickAndMortyLocalDataSource,
            refreshPrefsDataSource,
            networkMonitor
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun initialize_skips_when_cache_is_fresh() = runTest {
        val test10minutes = 10 * 60 * 1000

        `when`(refreshPrefsDataSource.lastRefreshFlow()).thenReturn(flowOf(System.currentTimeMillis() - test10minutes))

        val action = episodesRemoteMediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, action)
    }

    @Test
    fun initialize_launches_when_cache_is_stale() = runTest {
        val test2hours = 2 * 60 * 60 * 1000
        `when`(refreshPrefsDataSource.lastRefreshFlow()).thenReturn(flowOf(System.currentTimeMillis() - test2hours))

        val action = episodesRemoteMediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, action)
    }

    @Test
    fun refresh_offline_returnsSuccess_without_network_call() = runTest {
        `when`(networkMonitor.isConnected()).thenReturn(false)

        val result = episodesRemoteMediator.load(LoadType.REFRESH, emptyPagingState())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        verifyNoInteractions(rickAndMortyRemoteDataSource)
        verify(rickAndMortyLocalDataSource, never()).clearEpisodes()
        verify(pageRemoteKeyLocalDataSource, never()).clear(anyString())
    }

    @Test
    fun refresh_online_fetches_page1_and_writes_db_and_sets_lastRefresh() = runTest {
        `when`(networkMonitor.isConnected()).thenReturn(true)
        `when`(refreshPrefsDataSource.lastRefreshFlow()).thenReturn(flowOf(0L)) // value unused in load()

        `when`(rickAndMortyRemoteDataSource.getEpisodes(1)).thenReturn(
            ApiResult.Success(
                pageDto(
                    next = "https://rickandmortyapi.com/api/episode?page=2",
                    results = listOf(
                        episodeDto(id = 1, name = "Pilot"),
                        episodeDto(id = 2, name = "Lawnmower Dog")
                    )
                )
            )
        )

        `when`(pageRemoteKeyLocalDataSource.getRemoteKeys(anyString())).thenReturn(LocalResult.Empty)

        val result = episodesRemoteMediator.load(LoadType.REFRESH, emptyPagingState())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        verify(rickAndMortyLocalDataSource).clearEpisodes()
        verify(pageRemoteKeyLocalDataSource).clear(anyString())
        verify(rickAndMortyLocalDataSource).insertEpisodes(check { list ->
            assertEquals(2, list.size)
            assertEquals(1, list[0].id)
            assertEquals("Pilot", list[0].name)
        })

        verify(pageRemoteKeyLocalDataSource).upsert(check { keys: PageRemoteKeysEntity ->
            assertEquals(2, keys.nextPage)
            assertEquals("episodes", keys.label) // assuming your key label resolves to "episodes"
        })

        verify(refreshPrefsDataSource).setLastRefresh(anyLong())
    }

    @Test
    fun append_without_keys_returns_endOfPagination_true() = runTest {
        `when`(networkMonitor.isConnected()).thenReturn(true)
        `when`(pageRemoteKeyLocalDataSource.getRemoteKeys(anyString())).thenReturn(LocalResult.Empty)

        val result = episodesRemoteMediator.load(LoadType.APPEND, emptyPagingState())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun append_with_keys_fetches_next_and_sets_end_true_when_no_more() = runTest {
        `when`(networkMonitor.isConnected()).thenReturn(true)
        `when`(pageRemoteKeyLocalDataSource.getRemoteKeys(anyString())).thenReturn(
            LocalResult.Success(PageRemoteKeysEntity(label = "episodes", nextPage = 2, lastUpdated = 0L))
        )

        `when`(rickAndMortyRemoteDataSource.getEpisodes(2)).thenReturn(
            ApiResult.Success(
                pageDto(
                    next = null,
                    results = listOf(episodeDto(id = 3, name = "Anatomy Park"))
                )
            )
        )

        val result = episodesRemoteMediator.load(LoadType.APPEND, emptyPagingState())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        verify(rickAndMortyLocalDataSource).insertEpisodes(check { list ->
            assertEquals(1, list.size)
            assertEquals(3, list[0].id)
        })
        verify(pageRemoteKeyLocalDataSource).upsert(
            check { keys: PageRemoteKeysEntity ->
                assertEquals(null, keys.nextPage)
                assertEquals("episodes", keys.label)
            }
        )
    }


    @Test
    fun append_keys_error_bubbles_as_MediatorResult_Error() = runTest {
        `when`(networkMonitor.isConnected()).thenReturn(true)
        `when`(pageRemoteKeyLocalDataSource.getRemoteKeys(anyString())).thenReturn(LocalResult.Error(RuntimeException("keys-fail")))

        val result = episodesRemoteMediator.load(LoadType.APPEND, emptyPagingState())

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals("keys-fail", (result as RemoteMediator.MediatorResult.Error).throwable.message)
        verifyNoInteractions(rickAndMortyRemoteDataSource)
    }

    @Test
    fun refresh_remote_error_bubbles_as_MediatorResult_Error() = runTest {
        `when`(networkMonitor.isConnected()).thenReturn(true)
        `when`(rickAndMortyRemoteDataSource.getEpisodes(1)).thenReturn(ApiResult.Error(RuntimeException("remote-fail")))

        val result = episodesRemoteMediator.load(LoadType.REFRESH, emptyPagingState())

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals("remote-fail", (result as RemoteMediator.MediatorResult.Error).throwable.message)
    }
}