package com.zhdanon.nasaphotos.data.photo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.nasaphotos.entity.photo.ResponsePhoto

@JsonClass(generateAdapter = true)
data class ResponsePhotoDto(
    @Json(name = "photos") override val photos: List<PhotoDto>
) : ResponsePhoto
