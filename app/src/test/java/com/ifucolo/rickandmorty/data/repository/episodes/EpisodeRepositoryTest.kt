package com.ifucolo.rickandmorty.data.repository.episodes

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.local.source.RickAndMortyLocalDataSource
import com.ifucolo.rickandmorty.data.repository.paging.EpisodesRemoteMediator
import com.ifucolo.rickandmorty.data.repository.paging.PagerFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import javax.inject.Provider

@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class) // Use the Mockito test runner
class EpisodeRepositoryImplTest {


    // Mock the dependencies
    @Mock
    private lateinit var mockRemoteMediator: EpisodesRemoteMediator

    @Mock
    private lateinit var mockLocalDataSource: RickAndMortyLocalDataSource

    @Mock
    private lateinit var mockPagingSource: PagingSource<Int, EpisodeEntity>

    @Mock
    private lateinit var mockMediatorProvider: Provider<EpisodesRemoteMediator>

    @Mock
    private lateinit var mockPagerFactory: PagerFactory

    private lateinit var episodeRepository: EpisodeRepositoryImpl

    // Argument captors to capture the inputs to the factory
    @Captor
    private lateinit var configCaptor: ArgumentCaptor<PagingConfig>

    @Captor
    private lateinit var mediatorCaptor: ArgumentCaptor<EpisodesRemoteMediator>

    @Captor
    private lateinit var factoryCaptor: ArgumentCaptor<() -> PagingSource<Int, EpisodeEntity>>

    @Before
    fun setUp() {
        whenever(mockMediatorProvider.get()).doReturn(mockRemoteMediator)
        whenever(mockLocalDataSource.episodesPagingSource()).doReturn(mockPagingSource)

        whenever(mockPagerFactory.create(any(), any(), any())).doReturn(mock())

        episodeRepository = EpisodeRepositoryImpl(
            episodesRemoteMediator = mockMediatorProvider,
            local = mockLocalDataSource,
            pagerFactory = mockPagerFactory // <-- Pass the mock factory
        )
    }

    @Test
    fun `pagedEpisodes configures Pager with correct parameters`() = runTest {
        episodeRepository.pagedEpisodes().first() // Collect once to trigger the factory

        verify(mockPagerFactory).create(
            configCaptor.capture(),
            mediatorCaptor.capture(),
            factoryCaptor.capture()
        )

        val capturedConfig = configCaptor.value
        assertEquals(20, capturedConfig.pageSize)
        assertEquals(20, capturedConfig.initialLoadSize)
        assertEquals(2, capturedConfig.prefetchDistance)
        assertFalse(capturedConfig.enablePlaceholders)

        val capturedMediator = mediatorCaptor.value
        assertEquals(mockRemoteMediator, capturedMediator)

        val capturedPagingSourceFactory = factoryCaptor.value
        val pagingSource = capturedPagingSourceFactory() // Invoke the factory lambda
        assertEquals(mockPagingSource, pagingSource) // Ensure it returns the correct source
    }
}