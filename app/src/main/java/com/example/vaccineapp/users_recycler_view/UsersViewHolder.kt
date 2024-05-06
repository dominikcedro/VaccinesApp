package com.example.vaccineapp.users_recycler_view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R

class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val firstName: TextView = view.findViewById(R.id.firstName)
    val lastName: TextView = view.findViewById(R.id.lastName)
    val email: TextView = view.findViewById(R.id.email)
}