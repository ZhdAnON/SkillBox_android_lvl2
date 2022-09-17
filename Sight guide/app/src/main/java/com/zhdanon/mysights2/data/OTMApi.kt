package com.zhdanon.mysights2.data

import com.zhdanon.mysights2.data.model.CurrentSightDto
import com.zhdanon.mysights2.data.model.SightDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OTMApi {

    @GET("radius?src_geom=osm&src_attr=osm&kinds=cultural,historic,natural&format=json&apikey=$API_KEY")
    suspend fun getSightsList(
        @Query("radius") radius: Int,
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("limit") limit: Int
    ): List<SightDto>

    @GET("xid/{xid}?apikey=$API_KEY")
    suspend fun getSightInfo(
        @Path("xid") id: String
    ): CurrentSightDto

    companion object {
        private const val API_KEY = "5ae2e3f221c38a28845f05b626b180c87df86e3ed1f5d1d655687468"
    }
}