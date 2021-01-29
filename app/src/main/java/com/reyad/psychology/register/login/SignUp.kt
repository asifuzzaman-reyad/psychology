package com.reyad.psychology.register.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivitySignUpMainBinding
import com.reyad.psychology.register.Login
import dmax.dialog.SpotsDialog
import java.util.*

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpMainBinding
    private lateinit var dialog: SpotsDialog

    private var batch: String? = null
    private var id: String? = null
    private var blood: String? = null
    private var hall: String? = null
    private var pin: String? = null
    private var mobileNo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //load auto com
        loadBatchAndId()
        loadBloodGr()
        loadHall()

        //button sign in
        binding.btnSignUpSign.setOnClickListener {
            if (!validateStudentBatch() || !validateStudentId() || !validateStudentBloodGr()
                || !validateStudentHall() || !validateStudentPin() || !validateStudentMobile()
            ) {
                return@setOnClickListener
            } else {
                studentVerification()
            }

        }

        //button already
        binding.btnSignUpAlready.setOnClickListener {
            val loginIntent = Intent(this, EmailLogin::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            finish()
        }
    }

    //
    private fun studentVerification() {
        batch = binding.acBatchSign.text.toString()
        Log.i("signUp", "batch: $batch")
        id = binding.acIdSign.text.toString()
        blood = binding.acBloodSign.text.toString()
        hall = binding.acHallSign.text.toString()
        pin = binding.etPinSign.text.toString()
        mobileNo = binding.etMobileSign.text.toString()

        //
        dialog = SpotsDialog.Builder().setContext(this)
            .setCancelable(false)
            .setMessage("Verify Id and Pin...")
            .build() as SpotsDialog
        dialog.show()

        // check batch , id , mobile
        val db = FirebaseDatabase.getInstance().getReference("Tokens").child(batch.toString())
        val query = db.child(id.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Log.i("main", "Snapshot id: ${snapshot.key}")
                    binding.tilIdSign.error = null
                    binding.tilIdSign.isErrorEnabled = false

                    dialog.hide()

                    val studentItemsM = snapshot.child("token").value
                    Log.i("main", "token: $studentItemsM")

                    when {
                        pin == studentItemsM -> {
                            binding.tilPinSign.error = null
                            binding.tilPinSign.isErrorEnabled = false

                            dialog.dismiss()

                            val intent = Intent(this@SignUp, SignUp2::class.java).apply {
                                putExtra("batch", batch)
                                putExtra("id", snapshot.key)
                                putExtra("blood", blood)
                                putExtra("hall", hall)
                                putExtra("mobile", mobileNo)
                            }
                            startActivity(intent)

                        }
                        studentItemsM == "used" -> {
                            dialog.dismiss()
                            binding.tilPinSign.error = "Pin already used"
                            binding.tilPinSign.requestFocus()

                        }
                        else -> {
                            dialog.dismiss()
                            binding.tilPinSign.error = "Pin number not match"
                            binding.tilPinSign.requestFocus()
                        }
                    }

                }
                // if snap not exist
                else {
                    dialog.dismiss()
                    Log.i("main", "No Snapshot Found")
                    binding.tilIdSign.error = "Student Id not found"
                    binding.tilIdSign.requestFocus()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@SignUp,
                    "Database error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

        })

    }

    //********************************** auto complete ***********************************//
    // autoComplete batch
    private fun loadBatchAndId() {
        //list
        val batch = listOf("Batch 15", "Batch 14", "Batch 13", "Batch 12")
        val batch15 = listOf("206080", "196080")
        val batch14 = listOf("196080", "186080")
        val batch13 = listOf("186080", "176080")
        val batch12 = listOf("176080", "166080")

        //array adapter
        val adapterBatch = ArrayAdapter(applicationContext, R.layout.material_spinner_item, batch)

        val adapterBatch15 =
            ArrayAdapter(applicationContext, R.layout.material_spinner_item, batch15)
        val adapterBatch14 =
            ArrayAdapter(applicationContext, R.layout.material_spinner_item, batch14)
        val adapterBatch13 =
            ArrayAdapter(applicationContext, R.layout.material_spinner_item, batch13)
        val adapterBatch12 =
            ArrayAdapter(applicationContext, R.layout.material_spinner_item, batch12)

        //item click listener
        (binding.acBatchSign as AutoCompleteTextView?)?.setAdapter(adapterBatch)
        (binding.acBatchSign as AutoCompleteTextView?)?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->

                when (position) {
                    0 -> {
                        binding.acIdSign.setText("")
                        binding.tilIdSign.visibility = View.VISIBLE
                        (binding.acIdSign as AutoCompleteTextView?)?.setAdapter(adapterBatch15)
                    }

                    1 -> {
                        binding.acIdSign.setText("")
                        binding.tilIdSign.visibility = View.VISIBLE
                        (binding.acIdSign as AutoCompleteTextView?)?.setAdapter(adapterBatch14)
                    }

                    2 -> {
                        binding.acIdSign.setText("")
                        binding.tilIdSign.visibility = View.VISIBLE
                        (binding.acIdSign as AutoCompleteTextView?)?.setAdapter(adapterBatch13)
                    }

                    3 -> {
                        binding.acIdSign.setText("")
                        binding.tilIdSign.visibility = View.VISIBLE
                        (binding.acIdSign as AutoCompleteTextView?)?.setAdapter(adapterBatch12)
                    }

                }
            }
    }

    // autoComplete blood
    private fun loadBloodGr() {
        //list
        val bloodGrList = listOf(
            "O+",
            "O-",
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-"
        )
        //array adapter
        val adapterBlood = ArrayAdapter(this, R.layout.material_spinner_item, bloodGrList)
        (binding.acBloodSign as AutoCompleteTextView?)?.setAdapter(adapterBlood)
    }

    // autoComplete hall
    private fun loadHall() {
        //list
        val hallList = listOf(
            "Shaheed Abdur Rab Hall",
            "Pritilata Hall",
            "Shamsun Nahar Hall",
            "Jononetri Sheikh Hasina Hall",
            "Deshnetri Begum Khaleda Zia Hall",
            "Bangamata Sheikh Fazilatunnesa Mujib Hall"
        )
        //array adapter
        val adapterHall = ArrayAdapter(this, R.layout.material_spinner_item, hallList)
        (binding.acHallSign as AutoCompleteTextView?)?.setAdapter(adapterHall)
    }

    //********************************** validation **************************************//
    // student batch
    private fun validateStudentBatch(): Boolean {
        val value = binding.acBatchSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilBatchSign.error = "Field can not be empty"
                false
            }
            else -> {
                binding.tilBatchSign.error = null
                binding.tilBatchSign.isErrorEnabled = false
                true
            }
        }
    }

    // student id
    private fun validateStudentId(): Boolean {
        val value = binding.acIdSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilIdSign.error = "Field can not be empty"
                false
            }
            value.length < 8 -> {
                binding.tilIdSign.error = ("Student Id should contain 8 digits!")
                false
            }
            else -> {
                binding.tilIdSign.error = null
                binding.tilIdSign.isErrorEnabled = false
                true
            }
        }
    }

    // student blood
    private fun validateStudentBloodGr(): Boolean {
        val value = binding.acBloodSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilBloodSign.error = "Field can't be empty"
                false
            }
            else -> {
                binding.tilBloodSign.error = null
                binding.tilBloodSign.isErrorEnabled = false
                true
            }
        }
    }

    // student hall
    private fun validateStudentHall(): Boolean {
        val value = binding.acHallSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilHallSign.error = "Field can not be empty"
                false
            }
            else -> {
                binding.tilHallSign.error = null
                binding.tilHallSign.isErrorEnabled = false
                true
            }
        }
    }

    // student pin
    private fun validateStudentPin(): Boolean {
        val value = binding.etPinSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilPinSign.error = "Field can not be empty"
                false
            }
            value.length < 8 -> {
                binding.tilPinSign.error = ("Pin should be 8 digits!")
                false
            }
            else -> {
                binding.tilPinSign.error = null
                binding.tilPinSign.isErrorEnabled = false
                true
            }
        }
    }

    // student mobile
    private fun validateStudentMobile(): Boolean {
        val value = binding.etMobileSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilMobileSign.error = "Field can't be empty"
                false
            }
            value.length < 11 -> {
                binding.tilMobileSign.error = ("Mobile number should be 11 digits")
                false
            }
            else -> {
                binding.tilMobileSign.error = null
                binding.tilMobileSign.isErrorEnabled = false
                true
            }
        }
    }
}