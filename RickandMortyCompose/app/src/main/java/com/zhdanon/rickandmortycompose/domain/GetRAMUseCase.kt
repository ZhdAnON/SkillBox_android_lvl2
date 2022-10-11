package com.zhdanon.rickandmortycompose.domain

import com.zhdanon.rickandmortycompose.data.EpisodeDto
import com.zhdanon.rickandmortycompose.data.RaMRepository
import com.zhdanon.rickandmortycompose.data.characters.CharactersDto
import javax.inject.Inject

class GetRAMUseCase @Inject constructor(
    private val ramRepository: RaMRepository
) {
    suspend fun executeCharacters(
        count: Int,
        pages: Int,
        status: String,
        gender: String
    ): CharactersDto {
        return ramRepository.getCharactersList(count, pages, status, gender)
    }

    suspend fun executeEpisodeInfo(episodeId: String): List<EpisodeDto> {
        return ramRepository.getEpisodeInfo(episodeId)
    }
}