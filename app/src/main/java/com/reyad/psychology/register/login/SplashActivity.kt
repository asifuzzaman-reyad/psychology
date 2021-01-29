@file:Suppress("DEPRECATION")

package com.reyad.psychology.register.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.reyad.psychology.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // enable full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // delay 2 second
        Handler().postDelayed({
            val firstIntent = Intent(this, FirstActivity::class.java)
            startActivity(firstIntent)
            finish()
        }, 2000)
    }
}

