package com.zhdanon.mysights2.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.mysights2.entity.Preview
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PreviewDto(
    @Json(name = "height") override val height: Int?,
    @Json(name = "source") override val source: String?,
    @Json(name = "width") override val width: Int?
) : Preview, Parcelable
