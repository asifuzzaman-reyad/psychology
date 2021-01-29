package com.reyad.psychology.register.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.databinding.ActivityEmailLoginBinding
import dmax.dialog.SpotsDialog

class EmailLogin : AppCompatActivity() {

    private lateinit var binding: ActivityEmailLoginBinding

    private lateinit var dialog: SpotsDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginLoginEmail.setOnClickListener {

            if (!validateEmail() || !validatePassword()) {
                return@setOnClickListener
            }

            val email = binding.etEmailLoginEmail.text.toString()
            val password = binding.etPasswordLoginEmail.text.toString()

            //
            dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Verify Email and Password")
                .setCancelable(false)
                .build() as SpotsDialog

            dialog.show()

            loginWithEmailPassword(email, password)

        }

        binding.btnAlreadyLoginEmail.setOnClickListener {
            val signUpIntent = Intent(this, SignUp::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun loginWithEmailPassword(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("eLogin", "signInWithEmail:success")
                    dialog.dismiss()

                    //
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)

                } else {
                    // If sign in fails, display a message to the user.
                    dialog.dismiss()
                    Log.w("eLogin", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
    }

    // student email
    private fun validateEmail(): Boolean {
        val value = binding.etEmailLoginEmail.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilEmailLoginEmail.error = "Field can't be empty"
                false
            }
            else -> {
                binding.tilEmailLoginEmail.error = null
                binding.tilEmailLoginEmail.isErrorEnabled = false
                true
            }
        }
    }

    // student password
    private fun validatePassword(): Boolean {
        val value = binding.etPasswordLoginEmail.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilPasswordLoginEmail.error = "Field can't be empty"
                false
            }
            value.length < 8 -> {
                binding.tilPasswordLoginEmail.error = ("Password should contain 8 digits!")
                false
            }
            else -> {
                binding.tilPasswordLoginEmail.error = null
                binding.tilPasswordLoginEmail.isErrorEnabled = false
                true
            }
        }
    }
}