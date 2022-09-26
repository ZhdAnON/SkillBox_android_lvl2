package com.zhdanon.rickandmortyapi.data

import com.zhdanon.rickandmortyapi.data.characters.CharactersDto
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class RaMRepository @Inject constructor() {
    private val retrofit: RaMApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(RaMApi::class.java)

    suspend fun getCharacters(count: Int, pages: Int, status: String, gender: String): CharactersDto {
//        if (pages % 5 == 0) throw IllegalStateException("Что-то пошло не так...")
        return retrofit.getAllCharacters(count, pages, status, gender)
    }

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}