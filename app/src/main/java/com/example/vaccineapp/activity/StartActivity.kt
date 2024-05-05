package com.example.vaccineapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import com.example.vaccineapp.R

/**
 * Activity for the app's start screen.
 */
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // After 5 seconds, navigate from LogoFragment to LoginFragment
        Handler(Looper.getMainLooper()).postDelayed({
            val navController = findNavController(R.id.nav_host_fragment)
            if (navController.currentDestination?.id != R.id.loginFragment) {
                navController.navigate(R.id.action_logoFragment_to_loginFragment)
            }
        }, 3000) // 5 seconds delay
    }
}