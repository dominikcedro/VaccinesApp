package com.example.vaccineapp.recycler_view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R

class AdministeredVaccinationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val vaccineName: TextView = view.findViewById(R.id.vaccineName)
    val doseNumber: TextView = view.findViewById(R.id.doseNumber)
    val dateTime: TextView = view.findViewById(R.id.vaccineDate)
}