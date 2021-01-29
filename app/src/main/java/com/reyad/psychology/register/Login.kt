@file:Suppress("DEPRECATION", "DEPRECATION")

package com.reyad.psychology.register

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityLoginBinding
import com.reyad.psychology.register.login.SignUp

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

        // hook button
        loginBtn = findViewById(R.id.btn_login_login)
        alreadyBtn = findViewById(R.id.btn_already_login)

        //button login
        loginBtn.setOnClickListener {
            val mobile = binding.etMobileLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()


            if (!TextUtils.isEmpty(mobile) || !TextUtils.isEmpty(password)) {
                progressDialog.setTitle("User Login")
                progressDialog.setMessage("Please wait a while.\nChecking Mobile and Password... ")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()


            }
        }

        // button sign up
        alreadyBtn.setOnClickListener {
            //go to sign up1
            val signUp1Intent = Intent(this, SignUp::class.java)
            startActivity(signUp1Intent)
//            finish()
        }
    }




}
