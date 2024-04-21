package com.example.vaccineapp.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest

class AdministeredVaccinationAdapter(private val myDataset: Array<AdministeredVaccinationGetRequest>) :
    RecyclerView.Adapter<AdministeredVaccinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdministeredVaccinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.administered_vaccination_display_item, parent, false)

        return AdministeredVaccinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdministeredVaccinationViewHolder, position: Int) {
        holder.vaccineName.text = myDataset[position].vaccine.name
        holder.doseNumber.text = myDataset[position].doseNumber.toString()
        holder.dateTime.text = myDataset[position].dateTime
    }

    override fun getItemCount() = myDataset.size
}