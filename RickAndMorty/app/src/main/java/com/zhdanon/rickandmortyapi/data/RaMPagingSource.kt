package com.zhdanon.rickandmortyapi.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhdanon.rickandmortyapi.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortyapi.domain.GetRAMUseCase

class RaMPagingSource(
    private val getRAMUseCase: GetRAMUseCase,
    private val filterParams: FilterParams
) : PagingSource<Int, ResultCharacterDto>() {
    override fun getRefreshKey(state: PagingState<Int, ResultCharacterDto>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultCharacterDto> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            getRAMUseCase.executeCharacters(
                count = 10,
                pages = page,
                status = filterParams.paramStatus,
                gender = filterParams.paramGender
            )
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.results,
                    prevKey = null,
                    nextKey = if (it.results.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 1
    }
}