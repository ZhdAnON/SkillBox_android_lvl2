package com.zhdanon.nasaphotos.data.rover

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.rover.Camera

@JsonClass(generateAdapter = true)
data class CameraDto(
    @Json(name = "full_name") override val fullName: String,
    @Json(name = "id") override val id: Int,
    @Json(name = "name") override val name: String,
    @Json(name = "rover_id") override val roverId: Int
) : Camera