package com.ifucolo.rickandmorty.data.remote.api

import com.ifucolo.rickandmorty.data.remote.dto.CharacterDto
import com.ifucolo.rickandmorty.data.remote.dto.EpisodePageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("episode")
    suspend fun getEpisodes(@Query("page") page: Int): EpisodePageDto

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}