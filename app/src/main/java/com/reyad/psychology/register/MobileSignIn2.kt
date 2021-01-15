package com.reyad.psychology.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.reyad.psychology.databinding.ActivityMobileSignIn2Binding
import dmax.dialog.SpotsDialog
import java.util.concurrent.TimeUnit

class MobileSignIn2 : AppCompatActivity() {
    private lateinit var binding: ActivityMobileSignIn2Binding

    private var mVerificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private var dialog: SpotsDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileSignIn2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = SpotsDialog.Builder().setContext(this).build() as SpotsDialog?

        val mobile = intent.getStringExtra("mobile").toString()
        binding.tvMobileMain2.text = mobile

        //
        mAuth = FirebaseAuth.getInstance()
        sendVerificationCodeToUser(mobile)

        //
        binding.btnMain2Verify.setOnClickListener {
            val code = binding.pinViewMain2.text.toString()
            if (code.isNotEmpty()) {
                verifyVerificationCode(code)
            }
        }

    }

    //
    private fun sendVerificationCodeToUser(mobile: String) {
        dialog?.show()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+88$mobile", // Phone number to verify
            60,             // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            this,           // Activity (for callback binding)
            callbacks
        )
    }

    //
    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                mVerificationId = s
                //mResendToken = forceResendingToken
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    // set pin view
                    dialog?.dismiss()
                    binding.pinViewMain2.setText(code.toString())
                } else {
                    Toast.makeText(
                        this@MobileSignIn2,
                        "Please enter verification code",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@MobileSignIn2, e.message, Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(this@MobileSignIn2, e.message, Toast.LENGTH_LONG).show()
                }
            }

        }

    //
    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        //
        signInWithPhoneAuthCredential(credential)
    }

    //
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    //verification successful we will start the profile activity
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()

                    //
                    //signing the user
                    val batch = intent.getStringExtra("batch").toString()
                    val id = intent.getStringExtra("id").toString()
                    val blood = intent.getStringExtra("blood").toString()
                    val hall = intent.getStringExtra("hall").toString()
                    val mobile = intent.getStringExtra("mobile").toString()
                    val password = intent.getStringExtra("password").toString()

                    //firebase auth + database
                    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                    val usersRef = FirebaseDatabase.getInstance().getReference("Users_Mobile")

                    // user auth
                    val userHashMap: HashMap<Any, String> = HashMap()
                    userHashMap["batch"] = batch
                    userHashMap["id"] = id
                    userHashMap["mobile"] = mobile
                    userHashMap["password"] = password
                    userHashMap["userId"] = currentUser

                    // uidRef value
                    usersRef.child(currentUser)
                        .setValue(userHashMap)
                        .addOnCompleteListener {
                            //
                            val intent = Intent(this, FirstActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Data upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

}