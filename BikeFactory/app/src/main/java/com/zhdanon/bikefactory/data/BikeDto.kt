package com.zhdanon.bikefactory.data

import com.zhdanon.bikefactory.entity.Bike
import com.zhdanon.bikefactory.entity.Frame
import com.zhdanon.bikefactory.entity.Wheel

data class BikeDto(
    override val frame: Frame,
    override val wheels: List<Wheel>,
    override val logo: String
) : Bike