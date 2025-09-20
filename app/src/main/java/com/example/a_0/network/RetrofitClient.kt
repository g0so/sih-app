package com.example.a_0.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // IMPORTANT: For Android Emulator, localhost is 10.0.2.2
    // Replace XXXX with the HTTPS port from your .NET API
    private const val BASE_URL = "https://10.0.2.2:7224/"

    val instance: FittingApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(FittingApiService::class.java)
    }
}