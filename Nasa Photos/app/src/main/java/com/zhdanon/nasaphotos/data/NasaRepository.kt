package com.zhdanon.nasaphotos.data

import com.zhdanon.nasaphotos.data.photo.ResponsePhotoDto
import com.zhdanon.nasaphotos.entity.rover.ResponseRover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class NasaRepository @Inject constructor() {

    private val retrofit: NasaApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(NasaApi::class.java)

    suspend fun getRoversList(): ResponseRover {
        return withContext(Dispatchers.IO) {
            retrofit.getRoversList()
        }
    }

    suspend fun getPhotos(
        page: Int,
        roverName: String,
        camera: String?,
        sol: Int = 100
    ): ResponsePhotoDto {
        return withContext(Dispatchers.IO) {
            retrofit.getPhotos(
                page = page,
                roverName = roverName,
                sol = sol,
                camera = camera
            )
        }
    }

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/"
    }
}