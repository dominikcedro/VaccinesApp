package com.example.vaccineapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vaccineapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.admin_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)
        NavigationUI.setupWithNavController(bottomNav, navController)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.listOfUsers -> {
                    navController.navigate(R.id.ScheduledVaccinationsFragment) // TODO change to list of users
                    true
                }
                R.id.adminDashboard -> {
                    navController.navigate(R.id.mainMenuFragment) // TODO change to admin dashboard
                    true
                }
                R.id.usersVaccines -> {
                    navController.navigate(R.id.administeredVaccinationsFragment) // TODO change to users vaccines
                    true
                }
                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.adminDashboard
    }
}