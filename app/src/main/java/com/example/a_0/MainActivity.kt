package com.example.a_0 // <-- THIS LINE WAS CORRECTED

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a_0.network.RetrofitClient
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button
    private lateinit var tvStatus: TextView
    private lateinit var tvResult: TextView

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
        } else {
            getFittingDetails(result.contents)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan = findViewById(R.id.btnScan)
        tvStatus = findViewById(R.id.tvStatus)
        tvResult = findViewById(R.id.tvResult)

        btnScan.setOnClickListener {
            launchScanner()
        }
    }

    private fun launchScanner() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a fitting QR code")
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(true) // <-- ADD THIS LINE
        barcodeLauncher.launch(options)
    }

    private fun getFittingDetails(qrCode: String) {
        tvStatus.text = "Loading data for: $qrCode"
        tvResult.text = ""

        lifecycleScope.launch {
            try {
                val apiKey = "MySuperSecretKey_ChangeMe_12345"
                // IMPORTANT: Make sure the package name for RetrofitClient is also correct
                val response = com.example.a_0.network.RetrofitClient.instance.getFittingDetails(qrCode, apiKey)

                if (response.isSuccessful && response.body() != null) {
                    val fitting = response.body()!!
                    tvStatus.text = "Success!"

                    val resultBuilder = StringBuilder().apply {
                        appendLine("Vendor: ${fitting.vendorName}")
                        appendLine("Lot Number: ${fitting.lotNumber}")
                        appendLine("Status: ${fitting.currentStatus}")
                        appendLine("Warranty Expiry: ${fitting.warrantyExpiryDate}")
                        appendLine("\n--- Inspection History ---")
                        if (fitting.inspectionHistory.isEmpty()) {
                            appendLine("No inspections found.")
                        } else {
                            fitting.inspectionHistory.forEach {
                                appendLine(" - ${it.result} by ${it.inspectorName} on ${it.inspectionDate}")
                            }
                        }
                    }
                    tvResult.text = resultBuilder.toString()

                } else {
                    tvStatus.text = "Error: ${response.code()} ${response.message()}"
                    tvResult.text = "Could not find fitting with QR Code: $qrCode"
                }

            } catch (e: Exception) {
                tvStatus.text = "Network Failure"
                tvResult.text = "Could not connect to the server. Make sure the API is running and the IP/port are correct.\n\nError: ${e.message}"
            }
        }
    }
}