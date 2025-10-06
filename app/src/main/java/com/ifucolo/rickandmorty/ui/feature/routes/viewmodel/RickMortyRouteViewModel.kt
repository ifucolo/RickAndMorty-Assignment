package com.ifucolo.rickandmorty.ui.feature.routes.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ifucolo.rickandmorty.ui.feature.routes.RickMortyRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RickMortyRouteViewModel @Inject constructor() : ViewModel() {

    val backStack = mutableStateListOf<RickMortyRoutes>()

    init {
        cleanBackStackAndRestart()
    }

    fun cleanBackStackAndRestart() {
        backStack.clear()
        backStack.add(RickMortyRoutes.EpisodeList)
    }

    fun navigateTo(route: RickMortyRoutes) {
        backStack.add(route)
    }

    fun popBackStack(): Boolean {
        return if (backStack.size > 1) {
            backStack.removeLastOrNull() != null
        } else {
            false
        }
    }
}