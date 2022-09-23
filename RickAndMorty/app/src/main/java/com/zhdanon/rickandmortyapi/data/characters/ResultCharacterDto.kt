package com.zhdanon.rickandmortyapi.data.characters

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.rickandmortyapi.entity.ResultCharacter
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ResultCharacterDto(
    @Json(name = "created") override val created: String,           //--
    @Json(name = "episode") override val episode: List<String>,
    @Json(name = "gender") override val gender: String?,            //
    @Json(name = "id") override val id: Int,
    @Json(name = "image") override val image: String,               //
    @Json(name = "location") override val location: LocationDto?,   //
    @Json(name = "name") override val name: String,                 //
    @Json(name = "origin") override val origin: OriginDto,
    @Json(name = "species") override val species: String,
    @Json(name = "status") override val status: String?,
    @Json(name = "type") override val type: String,
    @Json(name = "url") override val url: String
) : ResultCharacter, Parcelable