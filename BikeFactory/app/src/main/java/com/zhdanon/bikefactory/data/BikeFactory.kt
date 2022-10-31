package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Bike
import javax.inject.Inject

class BikeFactory @Inject constructor(
    private val frameFactory: FrameFactory
) {
    fun createBike(logo: String, frameNumber: String, isMTB: Boolean): Bike {
        return BikeDto(
            frame = frameFactory.createFrame(frameNumber, isMTB),
            wheels = listOf(
                WheelSupplier.getWheel(isMTB),
                WheelSupplier.getWheel(isMTB)
            ),
            logo = logo
        )
    }
}