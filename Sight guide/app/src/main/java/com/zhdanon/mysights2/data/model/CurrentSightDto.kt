package com.zhdanon.mysights2.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.mysights2.entity.CurrentSight
import com.zhdanon.mysights2.entity.WikipediaExtracts
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CurrentSightDto(
    @Json(name = "address") override val address: AddressDto?,
    @Json(name = "image") override val image: String?,
    @Json(name = "name") override val name: String?,
    @Json(name = "point") override val point: PointDto?,
    @Json(name = "preview") override val preview: PreviewDto?,
    @Json(name = "wikidata") override val wikidata: String?,
    @Json(name = "wikipedia") override val wikipedia: String?,
    @Json(name = "xid") override val xid: String,
    @Json(name = "wikipedia_extracts") override val wikipediaExtracts: WikipediaExtractsDto?
) : CurrentSight, Parcelable