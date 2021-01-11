@file:Suppress("DEPRECATION", "DEPRECATION")

package com.reyad.psychology.register

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.reyad.psychology.R
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginBtn: MaterialButton
    private lateinit var alreadyBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //progress dialog
        progressDialog = ProgressDialog(this)

        // firebase auth
        mAuth = FirebaseAuth.getInstance()

        // hook button
        loginBtn = findViewById(R.id.btn_login_login)
        alreadyBtn = findViewById(R.id.btn_already_login)

        //button login
        loginBtn.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                progressDialog.setTitle("User Login")
                progressDialog.setMessage("Please wait a while.\nChecking Email and Password... ")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                firebaseAuthLogin(email, password)
            }
        }

        // button sign up
        alreadyBtn.setOnClickListener {
            //go to sign up1
            val signUp1Intent = Intent(this, SignUp1::class.java)
            startActivity(signUp1Intent)
//            finish()
        }
    }

    // firebase login
    private fun firebaseAuthLogin(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog.dismiss()

                //go to main activity
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainIntent)
                finish()

            } else {
                progressDialog.hide()
                Log.w("login", "Login With Email:failure", task.exception)
                Toast.makeText(
                    this, "Login failed.\nCheck email, password and try again ",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
}
