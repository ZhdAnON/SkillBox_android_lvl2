package com.zhdanon.bikefactory.entity

interface Bike {
    val frame: Frame
    val wheels: List<Wheel>
    val logo: String
}