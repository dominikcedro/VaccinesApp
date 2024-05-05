package com.example.vaccineapp.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        // Return the total number of pages.
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        // Return a new fragment instance for the page at the given position.
        return when (position) {
            0 -> AdministeredVaccinationsFragment()
            1 -> DashboardFragment()
            2 -> ScheduledVaccinationsFragment()
            else -> SettingsFragment()
        }
    }
}