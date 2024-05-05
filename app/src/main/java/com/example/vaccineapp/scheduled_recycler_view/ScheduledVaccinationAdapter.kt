package com.example.vaccineapp.scheduled_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.google.android.material.button.MaterialButton

/**
 * Adapter for the recycler view in the ScheduledVaccinationsFragment.
 */
class ScheduledVaccinationAdapter(private val myDataset: Array<ScheduledVaccinationGetRequest>, private val onFloatingButtonClicked: () -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_FLOATING_BUTTON = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == myDataset.size) VIEW_TYPE_FLOATING_BUTTON else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.scheduled_vaccination_display_item, parent, false) // create layout for item
            ScheduledViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_floating_button, parent, false)
            FloatingButtonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduledViewHolder) {
            holder.vaccineName.text = myDataset[position].vaccine.name
            holder.doseNumber.text = myDataset[position].doseNumber.toString()
            holder.dateTime.text = myDataset[position].dateTime
        } else if (holder is FloatingButtonViewHolder) {
            holder.floatingButton.setOnClickListener { onFloatingButtonClicked() }
        }
    }

    override fun getItemCount() = myDataset.size + 1

    class FloatingButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val floatingButton: MaterialButton = view.findViewById(R.id.floatingButton)
    }
}