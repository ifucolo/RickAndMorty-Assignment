package com.ifucolo.rickandmorty.data.remote.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

interface NetworkMonitor {
    fun isConnected(): Boolean
}
class NetworkMonitorImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
): NetworkMonitor {
    override fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}