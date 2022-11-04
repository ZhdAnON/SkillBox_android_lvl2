package com.zhdanon.bikefactory.data

class FactoryBicycle(
    private val frameFactory: FactoryFrame,
    private val wheelFactory: FactoryWheel
) {
    fun createBicycle(logo: String, frameNumber: String, color: Int): BikeDto {
        val frame = frameFactory.createFrame(frameNumber, color)
        val wheelFront = wheelFactory.createWheel()
        val wheelBack = wheelFactory.createWheel()
        return BikeDto(
            frame = frame,
            wheels = listOf(wheelFront, wheelBack),
            logo = logo
        )
    }
}