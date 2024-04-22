package com.example.vaccineapp.scheduled_recycler_view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R

class ScheduledViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val vaccineName: TextView = view.findViewById(R.id.vaccineName)
    val doseNumber: TextView = view.findViewById(R.id.doseNumber)
    val dateTime: TextView = view.findViewById(R.id.vaccineDate)
}