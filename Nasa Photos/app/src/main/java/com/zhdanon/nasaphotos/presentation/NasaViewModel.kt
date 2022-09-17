package com.zhdanon.nasaphotos.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhdanon.nasaphotos.data.NasaPhotosPagingSource
import com.zhdanon.nasaphotos.data.photo.PhotoDto
import com.zhdanon.nasaphotos.domain.GetNasaUseCase
import com.zhdanon.nasaphotos.entity.rover.Rover
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class NasaViewModel @Inject constructor(
    private val getNasaUseCase: GetNasaUseCase
) : ViewModel() {

    private val _rovers = MutableStateFlow<List<Rover>>(emptyList())
    val rovers = _rovers.asStateFlow()
    private val roversList = mutableListOf<Rover>()

    private val _cameras = MutableStateFlow<List<String>>(emptyList())
    val cameras = _cameras.asStateFlow()

    private val _sol = MutableSharedFlow<Int>(3534)
    val sol = _sol.asSharedFlow()

    private var currentRoverName = "Curiosity"
    private var currentCamera: String? = null
    private lateinit var currentRover: Rover
    private var _currentRoverMaxSol = 3543
    val currentRoverMaxSol get() = _currentRoverMaxSol

    val pagedPhotos: Flow<PagingData<PhotoDto>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            NasaPhotosPagingSource(
                getNasaUseCase,
                currentRoverName,
                sol = _currentRoverMaxSol,
                cameraName = currentCamera
            )
        }
    ).flow.cachedIn(viewModelScope)

    init {
        loadRoversList()
    }

    private fun loadRoversList() {
        viewModelScope.launch {
            val temp: List<Rover> = getNasaUseCase.executeRovers().rovers
            val tempRoversList = mutableListOf<Rover>()
            temp.map { rover ->
                tempRoversList.add(rover)
            }
            _rovers.value = tempRoversList
            currentRover = tempRoversList[0]
            roversList.addAll(tempRoversList)
            currentRover = roversList[0]
            _currentRoverMaxSol = currentRover.maxSol
            setCurrentRover(currentRover.name)
        }
    }

    fun setCurrentRover(selectRover: String) {
        currentRover = roversList.first { it.name == selectRover }
        currentRoverName = currentRover.name
        _currentRoverMaxSol = currentRover.maxSol
        roversList.map { rover ->
            val camerasList = mutableListOf<String>()
            if (rover.name == currentRoverName) {
                rover.cameras.map { camera ->
                    camerasList.add(camera.name)
                }
                _cameras.value = camerasList
            }
        }
    }

    fun setCurrentCamera(camera: String?) {
        Log.d(TAG, "setCurrentCamera: $camera")
        currentCamera = camera
    }

    fun setCurrentSol(sol: Int) {
        viewModelScope.launch {
            if (sol >= 0 && sol <= currentRover.maxSol) {
                _sol.emit(sol)
                _currentRoverMaxSol = sol
            }
        }
    }

    companion object {
        private const val TAG = "NasaViewModel"
    }
}