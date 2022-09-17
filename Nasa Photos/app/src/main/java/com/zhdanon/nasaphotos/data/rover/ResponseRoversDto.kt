package com.zhdanon.nasaphotos.data.rover

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.rover.ResponseRover

@JsonClass(generateAdapter = true)
data class ResponseRoversDto(
    @Json(name = "rovers") override val rovers: List<RoverDto>
) : ResponseRover