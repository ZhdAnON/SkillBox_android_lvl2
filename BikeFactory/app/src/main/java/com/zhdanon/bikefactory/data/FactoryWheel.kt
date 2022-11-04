package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Wheel
import kotlin.random.Random

class FactoryWheel {
    fun createWheel(): Wheel {
        return WheelDto(Random.nextInt(0,999999).toString())
    }
}