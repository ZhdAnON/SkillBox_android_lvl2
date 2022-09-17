package com.zhdanon.mysights2.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.mysights2.entity.Address
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressDto(
    @Json(name = "address") override val address: String?,
    @Json(name = "city") override val city: String?,
    @Json(name = "city_district") override val cityDistrict: String?,
    @Json(name = "country") override val country: String?,
    @Json(name = "road") override val road: String?,
    @Json(name = "state") override val state: String?
) : Address, Parcelable