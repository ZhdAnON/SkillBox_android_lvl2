package com.zhdanon.nasaphotos.data.photo

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.photo.CurrentCamera
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CurrentCameraDto(
    @Json(name = "full_name") override val fullName: String,
    @Json(name = "id") override val id: Int,
    @Json(name = "name") override val name: String,
    @Json(name = "rover_id") override val roverId: Int
) : CurrentCamera, Parcelable