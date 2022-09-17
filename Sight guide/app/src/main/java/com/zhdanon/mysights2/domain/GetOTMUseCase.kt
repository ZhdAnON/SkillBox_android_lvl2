package com.zhdanon.mysights2.domain

import com.zhdanon.mysights2.data.OTMRepository
import com.zhdanon.mysights2.data.model.CurrentSightDto
import com.zhdanon.mysights2.data.model.SightDto
import javax.inject.Inject

class GetOTMUseCase @Inject constructor(private val repository: OTMRepository) {
    suspend fun executeSights(
        lat: Double,
        lon: Double,
        limit: Int = 50,
        radius: Int = 10_000
    ): List<SightDto> {
        return repository.getSightsList(
            lat = lat,
            lon = lon,
            limit = limit,
            radius = radius
        )
    }

    suspend fun executeSightInfo(xid: String): CurrentSightDto {
        return repository.getSightInfo(xid)
    }
}