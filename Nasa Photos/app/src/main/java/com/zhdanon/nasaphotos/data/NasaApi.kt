package com.zhdanon.nasaphotos.data

import com.zhdanon.nasaphotos.data.photo.ResponsePhotoDto
import com.zhdanon.nasaphotos.data.rover.ResponseRoversDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaApi {
    @GET("rovers?api_key=$API_KEY")
    suspend fun getRoversList(): ResponseRoversDto

    @GET("rovers/{rover_name}/photos?api_key=$API_KEY")
    suspend fun getPhotos(
        @Path("rover_name") roverName: String,
        @Query("page") page: Int,
        @Query("sol") sol: Int = 100,
        @Query("camera") camera: String? = null
    ): ResponsePhotoDto

    companion object {
        private const val API_KEY = "Ln9mv0cz2uJaTOBVjsoG3aUeHxMNVZ5RW7hVfKkh"
    }
}