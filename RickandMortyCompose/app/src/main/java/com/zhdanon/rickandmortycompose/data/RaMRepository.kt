package com.zhdanon.rickandmortycompose.data

import com.zhdanon.rickandmortycompose.data.characters.CharactersDto
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class RaMRepository @Inject constructor() {
    private val retrofit: RaMApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(RaMApi::class.java)

    suspend fun getCharactersList(
        count: Int,
        pages: Int,
        status: String = "",
        gender: String = ""
    ): CharactersDto {
        return retrofit.getAllCharacters(count, pages, status, gender)
    }

    suspend fun getEpisodeInfo(episodeId: String) = retrofit.getEpisodeInfo(episodeId)

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}