package com.example.vaccineapp.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.vaccineapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val adminNavHostFragment = supportFragmentManager.findFragmentById(R.id.admin_nav_host_fragment) as NavHostFragment
        val adminNavController = adminNavHostFragment.navController
        val adminBottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)
        NavigationUI.setupWithNavController(adminBottomNav, adminNavController)

        adminBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.listOfUsers -> {
                    adminNavController.navigate(R.id.listOfUsersFragment)
                    true
                }
                R.id.adminDashboard -> {
                    adminNavController.navigate(R.id.adminDashboardFragment)
                    true
                }
                R.id.usersVaccines -> {
                    adminNavController.navigate(R.id.listOfVaccinesFragment)
                    true
                }
                R.id.settings -> {
                    adminNavController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
        adminBottomNav.selectedItemId = R.id.adminDashboardFragment
    }
}