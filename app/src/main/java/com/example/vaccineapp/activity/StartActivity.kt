package com.example.vaccineapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import com.example.vaccineapp.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // After 5 seconds, navigate from LogoFragment to LoginFragment
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_logoFragment_to_loginFragment)
        }, 5000) // 5 seconds delay
    }
}