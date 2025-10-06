package com.ifucolo.rickandmorty.data.repository.mock

import androidx.paging.PagingConfig
import androidx.paging.PagingState
import com.ifucolo.rickandmorty.data.local.entity.EpisodeEntity
import com.ifucolo.rickandmorty.data.remote.dto.EpisodeDto
import com.ifucolo.rickandmorty.data.remote.dto.EpisodePageDto
import com.ifucolo.rickandmorty.data.remote.dto.InfoDto

fun emptyPagingState(): PagingState<Int, EpisodeEntity> =
    PagingState(
        pages = listOf(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0
    )


fun pageDto(next: String?, results: List<EpisodeDto>) =
    EpisodePageDto(info = InfoDto(count = 1, pages = 1, next = next), results = results)

