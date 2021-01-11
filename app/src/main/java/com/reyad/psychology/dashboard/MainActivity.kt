package com.reyad.psychology.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.register.ProfileActivity
import com.reyad.psychology.databinding.ActivityMainBinding
import com.reyad.psychology.messenger.Messenger
import com.squareup.picasso.Picasso

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