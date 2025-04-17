package com.example.weatherpredictor

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=jamshedpur&appid=b2ad40928f619ca5ea974b80cf33c490

const val BaseUrl="https://api.openweathermap.org/"
const val apiId="b2ad40928f619ca5ea974b80cf33c490"
const val City="jamshedpur"

interface API {
    @GET("data/2.5/weather?appid=$apiId")
    fun getWeatherDAta(
        @Query("q") q:String,
        @Query("units") units:String
    ):Call<WeatherApp>
}


object WeatherService {
    val ApiInstance: API

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("$BaseUrl")
            .build()
        ApiInstance = retrofit.create(API::class.java)
    }

}

