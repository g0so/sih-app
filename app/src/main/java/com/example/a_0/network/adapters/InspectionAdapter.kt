package com.example.a_0.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a_0.R
import com.example.a_0.network.InspectionDto
import java.text.SimpleDateFormat
import java.util.Locale

class InspectionAdapter(private val inspections: List<InspectionDto>) :
    RecyclerView.Adapter<InspectionAdapter.InspectionViewHolder>() {

    // This inner class holds the views for a single list item.
    class InspectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resultTextView: TextView = itemView.findViewById(R.id.tvInspectionResult)
        val infoTextView: TextView = itemView.findViewById(R.id.tvInspectorInfo)
        val notesTextView: TextView = itemView.findViewById(R.id.tvInspectionNotes)
    }

    // Called by the RecyclerView to create a new ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inspection, parent, false)
        return InspectionViewHolder(view)
    }

    // Returns the total number of items in the list.
    override fun getItemCount(): Int {
        return inspections.size
    }

    // Called by the RecyclerView to display the data at a specific position.
    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {
        val inspection = inspections[position]

        // We can now use the date string directly
        val formattedDate = inspection.inspectionDate.substringBefore("T") // Gets just the "YYYY-MM-DD" part

        holder.resultTextView.text = "Result: ${inspection.result}"
        holder.infoTextView.text = "by ${inspection.inspectorName} on $formattedDate"

        if (inspection.notes.isNullOrEmpty()) {
            holder.notesTextView.visibility = View.GONE
        } else {
            holder.notesTextView.visibility = View.VISIBLE
            holder.notesTextView.text = "Notes: ${inspection.notes}"
        }
    }
}