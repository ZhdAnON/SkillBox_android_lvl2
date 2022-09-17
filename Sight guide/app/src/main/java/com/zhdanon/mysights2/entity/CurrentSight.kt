package com.zhdanon.mysights2.entity

interface CurrentSight {
    val address: Address?
    val image: String?
    val name: String?
    val point: Point?
    val preview: Preview?
    val wikidata: String?
    val wikipedia: String?
    val wikipediaExtracts: WikipediaExtracts?
    val xid: String
}