package com.zhdanon.nasaphotos.entity.photo

interface RoverManifest {
    val id: Int
    val landingDate: String
    val launchDate: String
    val name: String
    val status: String
}