package com.zhdanon.rickandmortycompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhdanon.rickandmortycompose.data.EpisodeDto
import com.zhdanon.rickandmortycompose.data.FilterParams
import com.zhdanon.rickandmortycompose.data.characters.CharactersDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.domain.GetRAMUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RaMViewModel @Inject constructor(
    private val ramUseCase: GetRAMUseCase
) : ViewModel() {
    private val filterParams = MutableStateFlow(
        FilterParams(
            status = "",
            gender = ""
        )
    )

    private lateinit var characterForDetail: ResultCharacterDto

    fun setCharacterForDetail(character: ResultCharacterDto) { characterForDetail = character }
    fun getCharacterForDetail() = characterForDetail

    private val _episodes = MutableStateFlow<List<EpisodeDto>>(emptyList())
    val episodes = _episodes.asStateFlow()

    private val _oneEpisode = MutableStateFlow(
        EpisodeDto(
            airDate = "",
            characters = emptyList(),
            created = "",
            episode = "",
            id = 0,
            name = "",
            url = ""
        )
    )
    val episode = _oneEpisode.asStateFlow()

    suspend fun loadCharacters(
        count: Int,
        page: Int,
        status: String,
        gender: String
    ): CharactersDto {
        return ramUseCase.executeCharacters(count, page, status, gender)
    }

    fun loadEpisodes(character: ResultCharacterDto) {
        val episodesId = StringBuilder()
        if (character.episode.size > 1) {
            character.episode.forEach {
                val lastIndex = it.lastIndexOf('/')
                val temp = it.removeRange(0, lastIndex + 1)
                if (episodesId.isBlank()) episodesId.append(temp) else episodesId.append(",$temp")
            }
        } else {
            val fullEpisodeName = character.episode.first()
            val lastIndex = fullEpisodeName.lastIndexOf('/')
            val temp = fullEpisodeName.removeRange(0, lastIndex + 1)
            episodesId.append(temp).append(",0")
        }
        viewModelScope.launch(Dispatchers.IO) {
            _episodes.value = ramUseCase.executeEpisodeInfo(episodesId.toString())
        }
    }

    fun setFilterParams(status: String, gender: String) {
        filterParams.value.paramStatus = status
        filterParams.value.paramGender = gender
    }

    fun getFilter() = filterParams
}