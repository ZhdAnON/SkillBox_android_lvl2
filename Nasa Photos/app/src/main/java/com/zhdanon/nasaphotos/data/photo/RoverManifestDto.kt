package com.zhdanon.nasaphotos.data.photo

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.photo.RoverManifest
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RoverManifestDto(
    @Json(name = "id") override val id: Int,
    @Json(name = "landing_date") override val landingDate: String,
    @Json(name = "launch_date") override val launchDate: String,
    @Json(name = "name") override val name: String,
    @Json(name = "status") override val status: String
) : RoverManifest, Parcelable