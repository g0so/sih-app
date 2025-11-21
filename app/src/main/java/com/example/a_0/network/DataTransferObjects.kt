package com.example.a_0.network

import java.io.Serializable

// This class holds the data for the new "Create Inspection" feature
data class CreateInspectionDto(
    val qrCode: String,
    val inspectorName: String,
    val result: String,
    val notes: String?
) : Serializable

data class InspectionDto(
    val inspectionDate: String,
    val inspectorName: String,
    val result: String,
    val notes: String?
) : Serializable

data class FittingDetailDto(
    val qrCode: String,
    val vendorName: String,
    val lotNumber: String,
    val supplyDate: String,
    val warrantyExpiryDate: String,
    val currentStatus: String,
    val inspectionHistory: List<InspectionDto>
) : Serializable

data class FittingAnalysisDto(
    val qrCode: String,
    val lotNumber: String,
    val currentStatus: String,
    val warrantyExpiryDate: String,
    val lastInspectionDate: String?,
    val aiSummary: String
) : Serializable

// --- ADD THIS NEW DATA CLASS ---
data class RailAnalysisDto(
    val sleeperCount: Int,
    val meanSpacing: Double,
    val standardDeviation: Double,
    val qualityScore: Double,
    val aiGeneratedReport: String
) : Serializable

