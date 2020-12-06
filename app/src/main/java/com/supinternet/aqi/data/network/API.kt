package com.supinternet.aqi.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.supinternet.aqi.data.network.model.ranking.Rankings
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AQIAPI {

    @GET("/countries")
    fun listCountries(): Deferred<Rankings>

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