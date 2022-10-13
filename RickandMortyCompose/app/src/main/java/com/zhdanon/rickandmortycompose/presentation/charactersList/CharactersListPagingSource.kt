package com.zhdanon.rickandmortycompose.presentation.charactersList

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel

class CharactersListPagingSource(
    private val viewModel: RaMViewModel
) : PagingSource<Int, ResultCharacterDto>() {
    override fun getRefreshKey(state: PagingState<Int, ResultCharacterDto>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultCharacterDto> =
        kotlin.runCatching {
            viewModel.loadCharacters(
                count = 10,
                page = params.key ?: 1,
                status = viewModel.getFilter().value.paramStatus,
                gender = viewModel.getFilter().value.paramGender
            ).results
        }.fold(
            onSuccess = { list ->
                LoadResult.Page(
                    data = list,
                    prevKey = params.key?.let { it - 1 },
                    nextKey = (params.key ?: 0) + 1
                )
            },
            onFailure = { throwable -> LoadResult.Error(throwable) }
        )

    companion object {
        fun page(viewModel: RaMViewModel) =
            Pager(PagingConfig(pageSize = 10)) { CharactersListPagingSource(viewModel) }.flow
    }
}