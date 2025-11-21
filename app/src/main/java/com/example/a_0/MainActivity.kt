package com.example.a_0

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var btnScan: Button
    private lateinit var tvStatus: TextView
    private lateinit var etManualQrCode: EditText
    private lateinit var btnSubmitManual: Button
    private lateinit var btnGenerateReport: Button // New Button

    private val TAG = "MyAppDebug"
    private val viewModel: MainViewModel by viewModels()

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
        } else {
            viewModel.fetchAllData(result.contents)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find all UI views
        btnScan = findViewById(R.id.btnScan)
        tvStatus = findViewById(R.id.tvStatus)
        etManualQrCode = findViewById(R.id.etManualQrCode)
        btnSubmitManual = findViewById(R.id.btnSubmitManual)
        btnGenerateReport = findViewById(R.id.btnGenerateReport) // Find new button

        Log.d(TAG, "MainActivity created.")
        setupObservers()

        btnScan.setOnClickListener {
            launchScanner()
        }

        btnSubmitManual.setOnClickListener {
            val qrCode = etManualQrCode.text.toString()
            if (qrCode.isNotBlank()) {
                viewModel.fetchAllData(qrCode)
            } else {
                Toast.makeText(this, "Please enter a code", Toast.LENGTH_SHORT).show()
            }
        }

        // Set listener for the new Generate Report button
        btnGenerateReport.setOnClickListener {
            // Tell the ViewModel to generate the report
            viewModel.generateRailAnalysisReport()
        }
    }

    private fun launchScanner() {
        // ... (this function is unchanged)
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE, ScanOptions.DATA_MATRIX)
        options.setPrompt("Scan a fitting code")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Idle -> {
                    tvStatus.text = "Ready..."
                }
                is UiState.Loading -> {
                    tvStatus.text = "Analyzing data..."
                }
                is UiState.Success -> {
                    Log.d(TAG, "Data received. Launching FittingDetailActivity.")
                    val intent = Intent(this, FittingDetailActivity::class.java).apply {
                        putExtra("FITTING_DETAILS", state.details)
                        putExtra("FITTING_ANALYSIS", state.analysis)
                    }
                    startActivity(intent)
                }
                is UiState.Error -> {
                    tvStatus.text = "Error: ${state.message}"
                }
                // New state for the rail report
                is UiState.ReportSuccess -> {
                    Log.d(TAG, "Report received. Launching RailAnalysisActivity.")
                    val intent = Intent(this, RailAnalysisActivity::class.java).apply {
                        putExtra("RAIL_REPORT", state.report)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}

