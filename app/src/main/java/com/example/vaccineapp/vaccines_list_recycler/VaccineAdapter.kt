package com.example.vaccineapp.vaccines_list_recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R

class VaccineAdapter(private val vaccineList: List<VaccineModel>) : RecyclerView.Adapter<VaccineAdapter.VaccineViewHolder>() {

    class VaccineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Initialize your views here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_list_row, parent, false)
        return VaccineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val currentItem = vaccineList[position]
        // TODO: Bind data to your views here
    }

    override fun getItemCount() = vaccineList.size
}