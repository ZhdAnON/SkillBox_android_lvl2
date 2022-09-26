package com.zhdanon.rickandmortyapi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhdanon.rickandmortyapi.data.FilterParams
import com.zhdanon.rickandmortyapi.data.RaMPagingSource
import com.zhdanon.rickandmortyapi.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortyapi.domain.GetRAMUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RaMViewModel @Inject constructor(
    private val getRaMUseCase: GetRAMUseCase
) : ViewModel() {

    lateinit var pagedAdapter: RaMAdapterRV

    private val filterParams = FilterParams(
        status = "",
        gender = ""
    )

    val pagedCharacters: Flow<PagingData<ResultCharacterDto>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            RaMPagingSource(getRaMUseCase, filterParams)
        }
    ).flow.cachedIn(viewModelScope)

    init {
        RaMPagingSource(getRaMUseCase, filterParams)
    }

    fun setFilterParams(status: String, gender: String) {
        filterParams.paramStatus = status
        filterParams.paramGender = gender
        pagedAdapter.refresh()
    }
}