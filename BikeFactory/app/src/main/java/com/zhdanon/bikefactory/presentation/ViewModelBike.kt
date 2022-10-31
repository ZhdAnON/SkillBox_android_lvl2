package com.zhdanon.bikefactory.presentation

import androidx.lifecycle.ViewModel
import com.zhdanon.bikefactory.data.BikeFactory
import com.zhdanon.bikefactory.entity.Bike
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelBike @Inject constructor(
    private val bikeFactory: BikeFactory
) : ViewModel() {
    private lateinit var currentBike: Bike

    private val _bike = Channel<Bike>()
    val bike = _bike.receiveAsFlow()

    fun createBikeDagger(
        frame: String,
        logo: String,
        isMTB: Boolean
    ) {
        currentBike = bikeFactory.createBike(logo, frame, isMTB)
        kotlinx.coroutines.runBlocking {
            _bike.send(currentBike)
        }
    }
}