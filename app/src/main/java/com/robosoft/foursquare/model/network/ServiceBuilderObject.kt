package com.robosoft.foursquare.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object
ServiceBuilderObject {
    val BASE_URL = "https://assesment-seven.vercel.app"
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service : Class<T>): T {
        return retrofit.create(service)
    }
}