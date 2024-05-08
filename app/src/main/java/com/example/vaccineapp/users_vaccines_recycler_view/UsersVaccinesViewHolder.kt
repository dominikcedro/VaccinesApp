package com.example.vaccineapp.users_vaccines_recycler_view

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R

class UsersScheduledViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val vaccineName: TextView = view.findViewById(R.id.vaccineName)
    val vaccineDate: TextView = view.findViewById(R.id.vaccineDate)
    val editButton: Button = view.findViewById(R.id.editButton)
}