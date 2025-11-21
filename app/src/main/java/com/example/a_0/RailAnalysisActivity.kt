package com.example.a_0

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a_0.network.RailAnalysisDto

class RailAnalysisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rail_analysis)

        // Get the report data that was passed from MainActivity
        val railReport = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("RAIL_REPORT", RailAnalysisDto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("RAIL_REPORT") as? RailAnalysisDto
        }

        // Find the TextView for the report content
        val tvReportContent: TextView = findViewById(R.id.tvReportContent)

        // If the report exists, display it. Otherwise, show an error.
        if (railReport != null) {
            tvReportContent.text = railReport.aiGeneratedReport
        } else {
            tvReportContent.text = "Error: Could not load the analysis report."
        }
    }
}

