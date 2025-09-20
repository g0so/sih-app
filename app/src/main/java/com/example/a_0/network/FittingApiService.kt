package com.example.a_0.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FittingApiService {
    @GET("/api/fittings/{qrCode}")
    suspend fun getFittingDetails(
        @Path("qrCode") qrCode: String,
        @Header("X-Api-Key") apiKey: String // Public GET, but we'll add key for future use
    ): Response<FittingDetailDto>

    // We'll add the POST for inspections later
}