package com.example.a_0

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a_0.network.FittingAnalysisDto
import com.example.a_0.network.FittingDetailDto
import com.example.a_0.network.RailAnalysisDto
import com.example.a_0.network.RetrofitClient
import kotlinx.coroutines.launch

// Add a new state for the report success
sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val details: FittingDetailDto, val analysis: FittingAnalysisDto) : UiState()
    data class ReportSuccess(val report: RailAnalysisDto) : UiState() // New State
    data class Error(val message: String) : UiState()
}

class MainViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> = _uiState

    fun fetchAllData(qrCode: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val apiKey = "MySuperSecretKey_ChangeMe_12345"

                val detailsResponse = RetrofitClient.instance.getFittingDetails(qrCode, apiKey)
                if (!detailsResponse.isSuccessful) {
                    _uiState.postValue(UiState.Error("API Error (Details): ${detailsResponse.code()}"))
                    return@launch
                }
                val fittingDetails = detailsResponse.body()!!

                val analysisResponse = RetrofitClient.instance.getFittingAnalysis(qrCode, apiKey)
                if (!analysisResponse.isSuccessful) {
                    _uiState.postValue(UiState.Error("API Error (Analysis): ${analysisResponse.code()}"))
                    return@launch
                }
                val fittingAnalysis = analysisResponse.body()!!

                _uiState.postValue(UiState.Success(fittingDetails, fittingAnalysis))
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error("Network Failure: ${e.message}"))
            }
        }
    }

    // --- ADD THIS NEW FUNCTION ---
    fun generateRailAnalysisReport() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.generateRailAnalysis()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.postValue(UiState.ReportSuccess(response.body()!!))
                } else {
                    _uiState.postValue(UiState.Error("API Error (Report): ${response.code()}"))
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error("Network Failure: ${e.message}"))
            }
        }
    }
}

