package com.example.a_0.network

import java.util.Date

// Note: These data classes must match the DTOs from our .NET API

data class InspectionDto(
    val inspectionDate: Date,
    val inspectorName: String,
    val result: String,
    val notes: String?
)

data class FittingDetailDto(
    val qrCode: String,
    val vendorName: String,
    val lotNumber: String,
    val supplyDate: String, // Keep as String for simplicity
    val warrantyExpiryDate: String, // Keep as String for simplicity
    val currentStatus: String,
    val inspectionHistory: List<InspectionDto>
)