package com.zhdanon.mysights2.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.mysights2.entity.Sight
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SightDto(
    @Json(name = "dist") override val dist: Double,
    @Json(name = "name") override val name: String?,
    @Json(name = "point") override val point: PointDto,
    @Json(name = "wikidata") override val wikidata: String?,
    @Json(name = "xid") override val xid: String
) : Sight, Parcelable