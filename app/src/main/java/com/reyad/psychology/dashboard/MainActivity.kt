package com.reyad.psychology.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // hook bottom navigation
        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_bottom)
        //add nav with host
        bottomNavigationView.setupWithNavController(navController)

    }

}