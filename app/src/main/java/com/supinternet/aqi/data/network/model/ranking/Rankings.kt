package com.supinternet.aqi.data.network.model.ranking

data class Rankings(
    val countries: List<RankingsCountry>,
)

data class RankingsCountry(
    val station: Int,
    val countryCode: String,
    val countryName: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val aqi: Int,
    val date: String

)