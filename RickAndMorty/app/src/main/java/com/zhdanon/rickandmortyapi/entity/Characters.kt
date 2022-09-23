package com.zhdanon.rickandmortyapi.entity

import com.zhdanon.rickandmortyapi.data.characters.InfoDto

interface Characters {
    val info: InfoDto?
    val results: List<ResultCharacter>
}