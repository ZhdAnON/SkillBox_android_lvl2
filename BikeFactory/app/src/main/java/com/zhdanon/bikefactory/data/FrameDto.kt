package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Frame

data class FrameDto(
    override val number: String,
    override val color: Int
) : Frame