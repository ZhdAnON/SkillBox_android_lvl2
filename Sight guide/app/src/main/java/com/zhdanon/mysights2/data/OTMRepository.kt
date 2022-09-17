package com.zhdanon.mysights2.data

import com.zhdanon.mysights2.data.model.CurrentSightDto
import com.zhdanon.mysights2.data.model.SightDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class OTMRepository @Inject constructor() {

    private val retrofit: OTMApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(OTMApi::class.java)

    suspend fun getSightsList(
        lat: Double,
        lon: Double,
        limit: Int = 50,
        radius: Int = 10_000
    ): List<SightDto> {
        return withContext(Dispatchers.IO) {
            retrofit.getSightsList(
                lat = lat,
                lon = lon,
                limit = limit,
                radius = radius
            )
        }
    }

    suspend fun getSightInfo(
        xid: String
    ): CurrentSightDto {
        return withContext(Dispatchers.IO) {
            retrofit.getSightInfo(xid)
        }
    }

    companion object {
        private const val BASE_URL = "https://api.opentripmap.com/0.1/ru/places/"
    }
}