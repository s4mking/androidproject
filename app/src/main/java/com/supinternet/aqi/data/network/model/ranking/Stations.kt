package com.supinternet.aqi.data.network.model.ranking

data class Stations(
    val stations: List<Station>,
)

data class Station(
    val station: Int,
    val aqi: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val date: String

)