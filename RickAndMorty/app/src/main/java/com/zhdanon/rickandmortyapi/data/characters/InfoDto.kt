package com.zhdanon.rickandmortyapi.data.characters

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.rickandmortyapi.entity.Info
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class InfoDto(
    @Json(name = "count") override val count: Int?,
    @Json(name = "next") override val next: String?,
    @Json(name = "pages") override val pages: Int?
) : Info, Parcelable