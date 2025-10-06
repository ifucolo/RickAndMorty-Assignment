package com.ifucolo.rickandmorty.data.remote.dto

import androidx.core.net.toUri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodePageDto(
    val info: InfoDto,
    val results: List<EpisodeDto>
) {
    fun nextPage(): Int? =
        info.next?.toUri()?.getQueryParameter("page")?.toIntOrNull()
}

@Serializable
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)

@Serializable
data class EpisodeDto(
    val id: Int,
    val name: String,
    @SerialName("air_date") val airDate: String,
    val episode: String,
    val characters: List<String>
)