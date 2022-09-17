package com.zhdanon.nasaphotos.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhdanon.nasaphotos.data.photo.PhotoDto
import com.zhdanon.nasaphotos.domain.GetNasaUseCase
import javax.inject.Inject

private const val TAG = "NasaPhotosPagingSource"

class NasaPhotosPagingSource @Inject constructor(
    private val getNasaUseCase: GetNasaUseCase,
    private val roverName: String,
    private val cameraName: String? = null,
    private val sol: Int
) : PagingSource<Int, PhotoDto>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoDto>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoDto> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            getNasaUseCase.executePhotos(
                page = page,
                roverName = roverName,
                cameraName = cameraName,
                sol = sol
            )
        }.fold(
            onSuccess = {
                it.photos.map { photo ->
                    Log.d(TAG, "load: ${photo.id} - ${photo.imgSrc}")
                }
                Log.d(TAG, "load: ${it.photos}")
                LoadResult.Page(
                    data = it.photos,
                    prevKey = null,
                    nextKey = if (it.photos.isEmpty()) null else page + 1
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