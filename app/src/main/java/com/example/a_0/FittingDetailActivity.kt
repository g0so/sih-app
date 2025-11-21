package com.example.a_0

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a_0.adapters.InspectionAdapter
import com.example.a_0.network.FittingAnalysisDto
import com.example.a_0.network.FittingDetailDto

class FittingDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitting_detail)

        val fittingDetails = getSerializable(intent, "FITTING_DETAILS", FittingDetailDto::class.java)
        val fittingAnalysis = getSerializable(intent, "FITTING_ANALYSIS", FittingAnalysisDto::class.java)

        val tvAiSummary: TextView = findViewById(R.id.tvAiSummary)
        tvAiSummary.text = fittingAnalysis?.aiSummary ?: "AI analysis not available."

        if (fittingDetails != null) {
            val tvQrCode: TextView = findViewById(R.id.tvDetailQrCode)
            val tvVendor: TextView = findViewById(R.id.tvDetailVendor)
            val tvLotNumber: TextView = findViewById(R.id.tvDetailLotNumber)
            val tvWarranty: TextView = findViewById(R.id.tvDetailWarranty)
            val btnAddInspection: Button = findViewById(R.id.btnAddInspection) // Get the button

            tvQrCode.text = fittingDetails.qrCode
            tvVendor.text = fittingDetails.vendorName
            tvLotNumber.text = fittingDetails.lotNumber
            tvWarranty.text = fittingDetails.warrantyExpiryDate

            val rvInspections: RecyclerView = findViewById(R.id.rvInspections)
            rvInspections.layoutManager = LinearLayoutManager(this)
            rvInspections.adapter = InspectionAdapter(fittingDetails.inspectionHistory)

            // Set the click listener for the new button
            btnAddInspection.setOnClickListener {
                val intent = Intent(this, AddInspectionActivity::class.java)
                // Pass the QR Code to the new activity so it knows what to add an inspection for
                intent.putExtra("QR_CODE", fittingDetails.qrCode)
                startActivity(intent)
            }
        }
    }

    private fun <T : java.io.Serializable?> getSerializable(intent: android.content.Intent, key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(key, clazz)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(key) as? T
        }
    }
}

