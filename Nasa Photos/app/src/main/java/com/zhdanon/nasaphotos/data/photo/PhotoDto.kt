package com.zhdanon.nasaphotos.data.photo

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.photo.Photo
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PhotoDto(
    @Json(name = "camera") override val camera: CurrentCameraDto,
    @Json(name = "earth_date") override val earthDate: String,
    @Json(name = "id") override val id: Int,
    @Json(name = "img_src") override val imgSrc: String,
    @Json(name = "rover") override val rover: RoverManifestDto,
    @Json(name = "sol") override val sol: Int
) : Photo, Parcelable