package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Frame

class FactoryFrame {
    fun createFrame(number: String, color: Int): Frame {
        return FrameDto(number, color)
    }
}