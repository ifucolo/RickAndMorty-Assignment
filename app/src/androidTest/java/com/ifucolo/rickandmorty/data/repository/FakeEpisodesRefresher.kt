package com.ifucolo.rickandmorty.data.repository

import com.ifucolo.rickandmorty.data.repository.refresh.EpisodesRefresher
import javax.inject.Inject

class FakeEpisodesRefresher @Inject constructor() : EpisodesRefresher {
    override suspend fun refresh(pages: Int) {
        // Do nothing. This is a fake.
    }
}