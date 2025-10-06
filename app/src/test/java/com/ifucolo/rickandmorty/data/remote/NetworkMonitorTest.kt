package com.ifucolo.rickandmorty.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.test.core.app.ApplicationProvider
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitorImpl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowConnectivityManager
import org.robolectric.shadows.ShadowNetworkCapabilities


@RunWith(RobolectricTestRunner::class)
class NetworkMonitorTest {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var shadowConnectivityManager: ShadowConnectivityManager
    private lateinit var networkMonitor: NetworkMonitorImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowConnectivityManager = shadowOf(connectivityManager)
        networkMonitor = NetworkMonitorImpl(connectivityManager)
    }

    @Test
    fun has_internet_connection_returns_true_when_network_has_internet_capability() {
        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        shadowConnectivityManager.setNetworkCapabilities(
            connectivityManager.activeNetwork,
            networkCapabilities
        )

        val result = networkMonitor.isConnected()
        assertTrue(result)
    }

    @Test
    fun has_internet_connection_returns_false_when_network_has_no_internet_capability() {
        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        shadowConnectivityManager.setNetworkCapabilities(
            connectivityManager.activeNetwork,
            networkCapabilities
        )

        val result = networkMonitor.isConnected()

        assertFalse(result)
    }

    @Test
    fun has_internet_connection_returns_false_when_no_active_network() {
        shadowConnectivityManager.setActiveNetworkInfo(null)

        val result = networkMonitor.isConnected()
        assertFalse(result)
    }
}