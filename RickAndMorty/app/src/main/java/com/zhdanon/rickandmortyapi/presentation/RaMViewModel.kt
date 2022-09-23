package com.zhdanon.rickandmortyapi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhdanon.rickandmortyapi.data.RaMPagingSource
import com.zhdanon.rickandmortyapi.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortyapi.domain.GetRAMUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RaMViewModel @Inject constructor(
    private val getRaMUseCase: GetRAMUseCase
) : ViewModel() {
    val pagedCharacters: Flow<PagingData<ResultCharacterDto>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            RaMPagingSource(getRaMUseCase)
        }
    ).flow.cachedIn(viewModelScope)

    init {
        RaMPagingSource(getRaMUseCase)
    }
}