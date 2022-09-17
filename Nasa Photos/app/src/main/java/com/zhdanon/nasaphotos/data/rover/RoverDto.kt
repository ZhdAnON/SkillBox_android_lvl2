package com.zhdanon.nasaphotos.data.rover

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.rover.Rover

@JsonClass(generateAdapter = true)
data class RoverDto(
    @Json(name = "cameras") override val cameras: List<CameraDto>,
    @Json(name = "id") override val id: Int,
    @Json(name = "landing_date") override val landingDate: String,
    @Json(name = "launch_date") override val launchDate: String,
    @Json(name = "max_date") override val maxDate: String,
    @Json(name = "max_sol") override val maxSol: Int,
    @Json(name = "name") override val name: String,
    @Json(name = "status") override val status: String,
    @Json(name = "total_photos") override val totalPhotos: Int
) : Rover