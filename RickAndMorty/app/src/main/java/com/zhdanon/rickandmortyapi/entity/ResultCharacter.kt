package com.zhdanon.rickandmortyapi.entity

import com.zhdanon.rickandmortyapi.data.characters.LocationDto
import com.zhdanon.rickandmortyapi.data.characters.OriginDto

interface ResultCharacter {
    val created: String
    val episode: List<String>
    val gender: String?
    val id: Int
    val image: String
    val location: LocationDto?
    val name: String
    val origin: OriginDto
    val species: String
    val status: String?
    val type: String
    val url: String
}