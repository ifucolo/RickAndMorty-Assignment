package com.ifucolo.rickandmorty.data.work


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.ifucolo.rickandmorty.data.datastore.RefreshPrefsDataSource
import com.ifucolo.rickandmorty.data.repository.refresh.EpisodesRefresher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class RefreshEpisodesWorkerTest {

    @Mock
    private lateinit var mockRefresher: EpisodesRefresher

    @Mock
    private lateinit var mockPrefs: RefreshPrefsDataSource

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockRefresher = org.mockito.Mockito.mock(EpisodesRefresher::class.java)
        mockPrefs = org.mockito.Mockito.mock(RefreshPrefsDataSource::class.java)
    }

    @Test
    fun `doWork returns Success when refresher and prefs succeed`() = runTest {
        val worker = TestListenableWorkerBuilder<RefreshEpisodesWorker>(context)
            .setWorkerFactory(TestRefreshWorkerFactory(mockRefresher, mockPrefs))
            .build()

        // WHEN: The doWork method is called
        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)

        verify(mockRefresher, times(1)).refresh(any())

        verify(mockPrefs, times(1)).setLastRefresh(any())
    }

    @Test
    fun `doWork returns Retry when refresher throws an exception`() = runTest {
        whenever(mockRefresher.refresh(any())).doThrow(RuntimeException("Network failed"))

        val worker = TestListenableWorkerBuilder<RefreshEpisodesWorker>(context)
            .setWorkerFactory(TestRefreshWorkerFactory(mockRefresher, mockPrefs))
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)

        verifyNoInteractions(mockPrefs)
    }
}

/**
 * A custom WorkerFactory for testing .
 * It tells the TestListenableWorkerBuilder how to create an instance of your worker
 */
class TestRefreshWorkerFactory(
    private val refresher: EpisodesRefresher,
    private val prefs: RefreshPrefsDataSource
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return RefreshEpisodesWorker(
            context = appContext,
            params = workerParameters,
            refresher = refresher,
            prefs = prefs
        )
    }
}
