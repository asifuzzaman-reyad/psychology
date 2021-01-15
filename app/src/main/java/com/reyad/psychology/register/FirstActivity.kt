@file:Suppress("DEPRECATION")

package com.reyad.psychology.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.databinding.ActivityFirst1Binding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirst1Binding

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirst1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // enable full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //button login
        binding.mBtnAlreadyFirst.setOnClickListener {
            val loginIntent = Intent(this, EmailLogin::class.java)
            startActivity(loginIntent)
        }

        //button create new acc
        binding.mBtnCreateFirst.setOnClickListener {
            val register1Intent = Intent(this, SignUp::class.java)
            startActivity(register1Intent)
        }
    }

    // on start -> user checking
    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            sendToMainActivity()
        } else {
            Log.d("firstActivity", "sign up first...")
        }
//        updateUI(currentUser)
    }

    // sent to main
    private fun sendToMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}