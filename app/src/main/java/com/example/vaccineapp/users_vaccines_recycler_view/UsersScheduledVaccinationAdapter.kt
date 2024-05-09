package com.example.vaccineapp.users_vaccines_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.Vaccine
class UsersScheduledVaccinationAdapter(
    private val vaccines: List<ScheduledVaccinationGetRequest>,
    private val onEditClick: (Long) -> Unit  // Change this line
) : RecyclerView.Adapter<UsersScheduledViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersScheduledViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_scheduled_vaccine, parent, false)
        return UsersScheduledViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersScheduledViewHolder, position: Int) {
        val vaccine = vaccines[position]
        holder.vaccineName.text = vaccines[position].vaccine.name
        holder.vaccineDate.text = vaccine.dateTime
        holder.editButton.setOnClickListener { onEditClick(vaccine.id) }  // This line is now correct
    }

    override fun getItemCount() = vaccines.size

}