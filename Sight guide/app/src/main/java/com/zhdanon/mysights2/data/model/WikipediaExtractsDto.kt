package com.zhdanon.mysights2.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.mysights2.entity.WikipediaExtracts
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WikipediaExtractsDto(
    @Json(name = "html") override val html: String?,
    @Json(name = "text") override val text: String?,
    @Json(name = "title") override val title: String?
): WikipediaExtracts, Parcelable
