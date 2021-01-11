package com.reyad.psychology.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.databinding.ActivitySignUp2Binding

private const val TAG = "sign up2"

class SignUp2 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp2Binding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    private lateinit var progressBar: ProgressBar

    private var name: String? = null
    private var email: String? = null
    private var password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // init firebase auth
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        //
        progressBar = binding.progressBarSignUp2

        // button sign up
        binding.btnSignUp2SignUp.setOnClickListener {
            Log.i(TAG, "Register ....")

            name = binding.etSignUp2Name.text.toString()
            email = binding.etSignUp2Email.text.toString()
            password = binding.etSignUp2Password.text.toString()


            if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                //progressbar
                progressBar.visibility = View.VISIBLE

                //
                firebaseAuthRegister(name!!, email!!, password!!)
            }
        }

        //button already
        binding.btnSignUp2Already.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
//            finish()
        }

    }

    // firebase Auth Register
    private fun firebaseAuthRegister(name: String, email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    progressBar.visibility = View.GONE

                    //get data from sign up1
                    val batch = intent.getStringExtra("batch").toString()
                    val id = intent.getStringExtra("id").toString()
                    val hall = intent.getStringExtra("hall").toString()
                    val mobile = intent.getStringExtra("mobile").toString()


                    //firebase auth + database
                    val currentUser = mAuth.currentUser!!.uid
                    val usersRef = mDatabase.reference.child("Users")

                    // user auth
                    val userHashMap: HashMap<Any, String> = HashMap()
                    userHashMap["batch"] = batch
                    userHashMap["name"] = name
                    userHashMap["id"] = id
                    userHashMap["userId"] = currentUser

                    // uidRef value
                    usersRef.child(currentUser)
                        .setValue(userHashMap)

//                     update Students Data
                    updateStudentsData(batch, id, hall, mobile)

                    //go to main activity
                    goToMainActivity()

                } else {
                    progressBar.visibility = View.GONE
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
    }

    //  update Students Data
    private fun updateStudentsData(
        batch: String, id: String, hall: String, mobile: String
    ) {
        // user hashMap
        val userdata: MutableMap<String, Any> = HashMap()
        userdata["hall"] = hall
        userdata["mobile"] = mobile

        val studentRef = mDatabase.reference.child("Students")
        //batchRef value
        studentRef.child(batch)
            .child(id)
            .updateChildren(userdata)
    }

    //  go To MainActivity
    private fun goToMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainIntent)
        finish()
    }
}