package com.zhdanon.rickandmortyapi.domain

import com.zhdanon.rickandmortyapi.data.RaMRepository
import com.zhdanon.rickandmortyapi.data.characters.CharactersDto
import javax.inject.Inject

class GetRAMUseCase @Inject constructor(
    private val ramRepository: RaMRepository
) {
    suspend fun executeCharacters(count: Int, pages: Int, status: String, gender: String): CharactersDto {
        return ramRepository.getCharacters(count, pages, status, gender)
    }
}