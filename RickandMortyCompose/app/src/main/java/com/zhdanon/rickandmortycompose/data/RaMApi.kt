package com.zhdanon.rickandmortycompose.data

import com.zhdanon.rickandmortycompose.data.characters.CharactersDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RaMApi {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("count") count: Int,
        @Query("pages") pages: Int,
        @Query("status") status: String = "",
        @Query("gender") gender: String = ""
    ): CharactersDto

    @GET("episode")
    suspend fun getEpisodeInfo(): EpisodeDto
}