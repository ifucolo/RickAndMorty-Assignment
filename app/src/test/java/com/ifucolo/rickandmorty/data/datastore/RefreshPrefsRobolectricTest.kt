package com.ifucolo.rickandmorty.data.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RefreshPrefsRobolectricTest {

    private lateinit var refreshPrefsDataSource: RefreshPrefsDataSource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        refreshPrefsDataSource = RefreshPrefsDataSourceImpl(context)
    }

    @Test
    fun savesAndReads() = runTest {
        refreshPrefsDataSource.setLastRefresh(123L)
        val value = refreshPrefsDataSource.lastRefreshFlow().first()
        assertEquals(123L, value)
    }

    @Test
    fun noDateWasSaved() = runTest {
        val value = refreshPrefsDataSource.lastRefreshFlow().first()
        assertEquals(0L, value)
    }
}