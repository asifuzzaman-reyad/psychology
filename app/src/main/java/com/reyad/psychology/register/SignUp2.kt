package com.reyad.psychology.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.databinding.ActivitySignUp2Binding
import dmax.dialog.SpotsDialog

private const val TAG = "sign up2"

class SignUp2 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp2Binding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    private lateinit var dialog: SpotsDialog

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
        dialog = (SpotsDialog.Builder().setContext(this).build() as SpotsDialog?)!!

        // button sign up
        binding.btnSignUp2SignUp.setOnClickListener {
            Log.i(TAG, "Register ....")

            name = binding.etSignUp2Name.text.toString()
            email = binding.etSignUp2Email.text.toString()
            password = binding.etSignUp2Password.text.toString()


            if (!validateUserName() || !validateEmail() || !validatePassword()) {
                return@setOnClickListener
            }
            //progressbar
            dialog.show()

            //
            firebaseAuthRegister(name!!, email!!, password!!)

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
                    dialog.dismiss()

                    //get data from sign up1
                    val batch = intent.getStringExtra("batch").toString()
                    val id = intent.getStringExtra("id").toString()
                    val hall = intent.getStringExtra("hall").toString()
                    val mobile = intent.getStringExtra("mobile").toString()


                    //firebase auth + database
                    val currentUser = mAuth.currentUser!!.uid
                    val usersRef = mDatabase.reference.child("Users")

                    /////////////////////////////user auth ///////////////////////////////
                    val userHashMap: HashMap<Any, String> = HashMap()
                    userHashMap["batch"] = batch
                    userHashMap["name"] = name
                    userHashMap["id"] = id
                    userHashMap["userId"] = currentUser

                    // uidRef value
                    usersRef.child(currentUser).setValue(userHashMap)

                    /////////////////update Students Data /////////////////////////////////
                    updateStudentsData(batch, id, hall, mobile)

                    /////////////////// reset token ////////////////////////////////////////
                    val db = FirebaseDatabase.getInstance().getReference("Tokens").child(batch)
                    val query = db.child(id).child("token").setValue("used")

                    //go to main activity
                    goToMainActivity()

                } else {
                    dialog.dismiss()
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

    ///////////////////////////////////////////////////////////////////////////////////////////

    // student name
    private fun validateUserName(): Boolean {
        val value = binding.etSignUp2Name.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilSignUp2Name.error = "Field can't be empty"
                false
            }
            value.length < 4 -> {
                binding.tilSignUp2Name.error = ("Too small User name")
                false
            }
            else -> {
                binding.tilSignUp2Name.error = null
                binding.tilSignUp2Name.isErrorEnabled = false
                true
            }
        }
    }

    // student email
    private fun validateEmail(): Boolean {
        val value = binding.etSignUp2Email.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilSignUp2Email.error = "Field can't be empty"
                false
            }
            else -> {
                binding.tilSignUp2Email.error = null
                binding.tilSignUp2Email.isErrorEnabled = false
                true
            }
        }
    }

    // student password
    private fun validatePassword(): Boolean {
        val value = binding.etSignUp2Password.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilSignUp2Password.error = "Field can't be empty"
                false
            }
            value.length < 8 -> {
                binding.tilSignUp2Password.error = ("Password should contain 8 digits!")
                false
            }
            else -> {
                binding.tilSignUp2Password.error = null
                binding.tilSignUp2Password.isErrorEnabled = false
                true
            }
        }
    }
}