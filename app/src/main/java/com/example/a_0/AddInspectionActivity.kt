package com.example.a_0

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a_0.network.CreateInspectionDto
import com.example.a_0.network.RetrofitClient
import kotlinx.coroutines.launch

class AddInspectionActivity : AppCompatActivity() {

    private var qrCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inspection)

        // Get the QR Code passed from the previous screen
        qrCode = intent.getStringExtra("QR_CODE")

        // Find all the UI elements from our layout
        val etInspectorName: EditText = findViewById(R.id.etInspectorName)
        val rgResult: RadioGroup = findViewById(R.id.rgResult)
        val etNotes: EditText = findViewById(R.id.etNotes)
        val btnSubmit: Button = findViewById(R.id.btnSubmitInspection)

        btnSubmit.setOnClickListener {
            val inspectorName = etInspectorName.text.toString()
            val selectedRadioButtonId = rgResult.checkedRadioButtonId
            val notes = etNotes.text.toString()

            // --- Form Validation ---
            if (qrCode == null) {
                Toast.makeText(this, "Error: QR Code not found.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (inspectorName.isBlank()) {
                Toast.makeText(this, "Please enter an inspector name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedRadioButtonId == -1) {
                Toast.makeText(this, "Please select a result.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- End Validation ---

            val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
            val result = selectedRadioButton.text.toString()

            // Create the data object to send to the API
            val newInspection = CreateInspectionDto(
                qrCode = qrCode!!,
                inspectorName = inspectorName,
                result = result,
                notes = notes
            )

            // Call the function to submit the data
            submitInspection(newInspection)
        }
    }

    private fun submitInspection(inspection: CreateInspectionDto) {
        // Run the network call in a coroutine
        lifecycleScope.launch {
            try {
                val apiKey = "MySuperSecretKey_ChangeMe_12345" // Use your actual API key
                val response = RetrofitClient.instance.createInspection(apiKey, inspection)

                if (response.isSuccessful) {
                    Toast.makeText(this@AddInspectionActivity, "Inspection submitted successfully!", Toast.LENGTH_LONG).show()
                    // Close this screen and go back to the detail screen
                    finish()
                } else {
                    Toast.makeText(this@AddInspectionActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@AddInspectionActivity, "Network Failure: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
