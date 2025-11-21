package com.example.a_0.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FittingApiService {

    @GET("/api/fittings/{qrCode}")
    suspend fun getFittingDetails(
        @Path("qrCode") qrCode: String,
        @Header("X-Api-Key") apiKey: String
    ): Response<FittingDetailDto>

    @GET("/api/fittings/analysis/{qrCode}")
    suspend fun getFittingAnalysis(
        @Path("qrCode") qrCode: String,
        @Header("X-Api-Key") apiKey: String
    ): Response<FittingAnalysisDto>

    @POST("/api/inspections")
    suspend fun createInspection(
        @Header("X-Api-Key") apiKey: String,
        @Body inspection: CreateInspectionDto
    ): Response<Unit>

    // --- ADD THIS NEW FUNCTION ---
    @GET("/api/railanalysis/generate")
    suspend fun generateRailAnalysis(): Response<RailAnalysisDto>
}

