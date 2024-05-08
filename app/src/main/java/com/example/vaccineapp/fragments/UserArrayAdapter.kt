package com.example.vaccineapp.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.UserDetails

class UserArrayAdapter(context: Context, private val users: List<UserDetails>) :
    ArrayAdapter<UserDetails>(context, R.layout.user_dropdown_item, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_dropdown_item, parent, false)

        val userIdTextView = view.findViewById<TextView>(R.id.userId)
        val emailTextView = view.findViewById<TextView>(R.id.email)

        val user = users[position]
        userIdTextView.text = user.id.toString()
        emailTextView.text = user.email

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}