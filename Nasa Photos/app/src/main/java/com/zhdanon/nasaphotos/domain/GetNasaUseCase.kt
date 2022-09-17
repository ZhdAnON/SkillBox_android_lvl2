package com.zhdanon.nasaphotos.domain

import com.zhdanon.nasaphotos.data.NasaRepository
import com.zhdanon.nasaphotos.data.photo.ResponsePhotoDto
import com.zhdanon.nasaphotos.entity.rover.ResponseRover
import javax.inject.Inject

class GetNasaUseCase @Inject constructor(
    private val repository: NasaRepository
) {
    suspend fun executePhotos(
        page: Int,
        roverName: String,
        cameraName: String?,
        sol: Int
    ): ResponsePhotoDto {
        return repository.getPhotos(
            page = page,
            roverName = roverName,
            sol = sol,
            camera = cameraName
        )
    }

    suspend fun executeRovers(): ResponseRover {
        return repository.getRoversList()
    }
}