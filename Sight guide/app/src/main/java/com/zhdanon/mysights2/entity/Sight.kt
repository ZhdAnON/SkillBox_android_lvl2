package com.zhdanon.mysights2.entity

interface Sight {
    val dist: Double
    val name: String?
    val point: Point
    val wikidata: String?
    val xid: String
}