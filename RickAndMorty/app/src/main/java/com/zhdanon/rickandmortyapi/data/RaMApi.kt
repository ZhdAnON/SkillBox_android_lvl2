package com.zhdanon.rickandmortyapi.data

import com.zhdanon.rickandmortyapi.data.characters.CharactersDto
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
}