package com.zhdanon.nasaphotos.entity.photo

interface Photo {
    val camera: CurrentCamera
    val earthDate: String
    val id: Int
    val imgSrc: String
    val rover: RoverManifest
    val sol: Int
}