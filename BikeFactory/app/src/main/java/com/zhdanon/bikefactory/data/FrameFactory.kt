package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Frame
import javax.inject.Inject

class FrameFactory @Inject constructor() {
    fun createFrame(number: String, isMTB: Boolean): Frame {
        return if (isMTB) FrameMTB(number) else FrameRoad(number)
    }
}