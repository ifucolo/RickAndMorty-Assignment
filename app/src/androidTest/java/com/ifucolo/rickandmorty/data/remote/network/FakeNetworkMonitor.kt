package com.ifucolo.rickandmorty.data.remote.network

import javax.inject.Inject

class FakeNetworkMonitor @Inject constructor(): NetworkMonitor {
    override fun isConnected(): Boolean = true
}