package com.zhdanon.nasaphotos.entity.rover

interface Rover {
    val cameras: List<Camera>
    val id: Int
    val landingDate: String
    val launchDate: String
    val maxDate: String
    val maxSol: Int
    val name: String
    val status: String
    val totalPhotos: Int
}