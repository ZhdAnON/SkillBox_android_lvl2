package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Wheel
import kotlin.random.Random

object WheelSupplier {

    private val numberList = listOf<String>()

    fun getWheel(isMTB: Boolean): Wheel {
        var number: String
        while (true) {
            number = Random.nextInt(0, 999999).toString()
            if (numberList.contains(number)) continue else break
        }
        return if(isMTB) WheelMTB("MBW$number") else WheelRoad("RW$number")
    }
}