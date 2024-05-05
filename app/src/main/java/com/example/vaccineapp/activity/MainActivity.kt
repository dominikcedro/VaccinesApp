package com.example.vaccineapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.vaccineapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager2.widget.ViewPager2
import com.example.vaccineapp.fragments.SectionsPagerAdapter

/**
 * Main activity for the app.
 */
class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This app needs the POST_NOTIFICATION permission to send notifications. Would you like to grant this permission?")
                    .setPositiveButton("OK") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .setNegativeButton("No thanks", null)
                    .show()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askNotificationPermission()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(bottomNav, navController)


        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scheduled -> {
                    navController.navigate(R.id.ScheduledVaccinationsFragment)
                    true
                }
                R.id.dashboard -> {
                    navController.navigate(R.id.mainMenuFragment)
                    true
                }
                R.id.administered -> {
                    navController.navigate(R.id.administeredVaccinationsFragment)
                    true
                }
                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.dashboard



        val viewPager: ViewPager2 = findViewById<ViewPager2>(R.id.viewPager)

// Set up the ViewPager2 with the sections adapter.
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager.adapter = sectionsPagerAdapter

// Set up the ViewPager2's page change callback.
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.menu.getItem(position).isChecked = true
            }
        })

// Set up the BottomNavigationView's selected item listener.
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.administered -> viewPager.currentItem = 0
                R.id.dashboard -> viewPager.currentItem = 1
                R.id.scheduled -> viewPager.currentItem = 2
                R.id.settings -> viewPager.currentItem = 3
            }
            true
        }
    }
}