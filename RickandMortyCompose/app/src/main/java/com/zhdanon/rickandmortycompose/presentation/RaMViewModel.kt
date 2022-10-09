package com.zhdanon.rickandmortycompose.presentation

import androidx.lifecycle.ViewModel
import com.zhdanon.rickandmortycompose.data.FilterParams
import com.zhdanon.rickandmortycompose.data.characters.CharactersDto
import com.zhdanon.rickandmortycompose.domain.GetRAMUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RaMViewModel @Inject constructor(
    private val ramUseCase: GetRAMUseCase
) : ViewModel() {
    val filterParams = MutableStateFlow(
        FilterParams(
            status = "",
            gender = ""
        )
    )

    suspend fun loadCharacters(
        count: Int,
        page: Int,
        status: String,
        gender: String
    ): CharactersDto {
        return ramUseCase.executeCharacters(count, page, status, gender)
    }

    fun setFilterParams(status: String, gender: String) {
        filterParams.value.paramStatus = status
        filterParams.value.paramGender = gender
    }

    fun getFilter() = filterParams
}