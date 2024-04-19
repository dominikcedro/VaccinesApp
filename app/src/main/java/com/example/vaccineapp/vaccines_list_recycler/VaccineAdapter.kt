package com.example.vaccineapp.vaccines_list_recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R

class VaccineAdapter(private val vaccineList: List<VaccineModel>) : RecyclerView.Adapter<VaccineAdapter.VaccineViewHolder>() {

    class VaccineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvvaccineName = itemView.findViewById<TextView>(R.id.containerVaccineName)
        val tvRecommendedAge = itemView.findViewById<TextView>(R.id.tvRecommendedAge)
        val tvNumberDoses = itemView.findViewById<TextView>(R.id.tvNumberDoses)
        val tvRemainingDoses = itemView.findViewById<TextView>(R.id.tvRemainingDoses)

        // these below are for including data in the recycler view
        val containerRecommendedAge = itemView.findViewById<TextView>(R.id.containerRecommendedAge)
        val containerNumberDoses = itemView.findViewById<TextView>(R.id.containerNumberDoses)
        val containerDosesRemaining = itemView.findViewById<TextView>(R.id.containerDosesRemaining)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_list_row, parent, false)
        return VaccineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val currentItem = vaccineList[position]
        holder.tvvaccineName.text = currentItem.name
        holder.containerRecommendedAge.text = currentItem.recommendedAge.toString()
        holder.containerNumberDoses.text = currentItem.doses.toString()
        holder.containerDosesRemaining.text = currentItem.remainingDoses.size.toString()
    }

    override fun getItemCount() = vaccineList.size
}