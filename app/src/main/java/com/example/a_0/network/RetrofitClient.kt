package com.example.a_0.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // This should be your current, active ngrok URL
    private const val BASE_URL = "https://8f9ca4297349.ngrok-free.app"

    // --- NEW: Create a custom OkHttpClient with a longer timeout ---
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Time to establish the connection
        .readTimeout(30, TimeUnit.SECONDS)    // Time to wait for data
        .writeTimeout(30, TimeUnit.SECONDS)   // Time to send data
        .build()
    // --- End of new code ---

    val instance: FittingApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // <-- ATTACH THE NEW CLIENT WITH THE LONGER TIMEOUT
            .build()
        retrofit.create(FittingApiService::class.java)
    }
}
