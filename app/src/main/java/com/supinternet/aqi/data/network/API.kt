package com.supinternet.aqi.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.supinternet.aqi.data.network.model.ranking.Rankings
import com.supinternet.aqi.data.network.model.ranking.Station
import com.supinternet.aqi.data.network.model.ranking.Stations
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AQIAPI {

    @GET("/countries")
    fun listCountries(): Deferred<Rankings>

    @GET("/search")
    fun listStationsAround(@Query(value = "query") param: String): Deferred<Stations>

    @GET("/search")
    fun listStationsLocalisation(@Query(value = "latlng") param: String): Deferred<Stations>

    companion object Factory {
        @Volatile
        private var retrofit: Retrofit? = null

        private const val BASE_URL: String = "https://aqi.formation-android.fr/"

        @Synchronized
        fun getInstance(): AQIAPI {
            retrofit = retrofit ?: buildRetrofit()
            return retrofit!!.create(AQIAPI::class.java)
        }

        private fun buildRetrofit() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

}