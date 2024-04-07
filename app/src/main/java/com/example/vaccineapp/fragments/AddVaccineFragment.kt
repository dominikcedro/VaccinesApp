package com.example.vaccineapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.vaccineapp.R
import com.example.vaccineapp.fragments.DatePickFragment

class AddVaccineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_vaccine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etChoseVaccine = view.findViewById<EditText>(R.id.etChoseVaccine)
        val etChoseDate = view.findViewById<EditText>(R.id.etChoseDate)
        }

    }