package com.zhdanon.mysights2.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.mysights2.entity.Point
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PointDto(
    @Json(name = "lat") override val lat: Double,
    @Json(name = "lon") override val lon: Double
) : Point, Parcelable