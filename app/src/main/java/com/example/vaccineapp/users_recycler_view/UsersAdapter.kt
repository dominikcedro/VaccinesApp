package com.example.vaccineapp.users_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.UserRow

class UsersAdapter(private val myDataset: Array<UserRow>) :
    RecyclerView.Adapter<UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_display_item, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = myDataset[position]
        holder.firstName.text = user.firstName
        holder.lastName.text = user.lastName
        holder.email.text = user.email
    }

    override fun getItemCount() = myDataset.size
}