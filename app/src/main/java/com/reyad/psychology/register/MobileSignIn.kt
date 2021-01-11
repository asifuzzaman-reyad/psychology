package com.reyad.psychology.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import com.reyad.psychology.databinding.ActivityMobileSignInBinding
import dmax.dialog.SpotsDialog
import java.util.*

class MobileSignIn : AppCompatActivity() {

    private lateinit var binding: ActivityMobileSignInBinding
    private var dialog: SpotsDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //load id and batch
        loadBatchAndId()
        loadHall()
        loadBloodGr()
        //
        binding.btnBirthMobileSign.setOnClickListener {
            loadBirthDate()
        }

        //button next
        binding.btnSignUpMobileSign.setOnClickListener {
            if (!validateStudentBatch() || !validateStudentId() || !validateStudentMobile()) {
                return@setOnClickListener
            } else {
                studentVerification()
            }

        }
    }

    //
    private fun studentVerification() {
        val batch = binding.acBatchMobileSign.text.toString()
        Log.i("main", "batch: $batch")
        val enteredId = binding.acIdMobileSign.text.toString()
        val enteredMobile = binding.etMobileMobileSign.text.toString()

        dialog = SpotsDialog.Builder().setContext(this).build() as SpotsDialog?
        dialog?.show()

        val db = FirebaseDatabase.getInstance().getReference("Tokens").child(batch)
        val query = db.child(enteredId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Log.i("main", "Snapshot id: ${snapshot.key}")
                    binding.tilIdMobileSign.error = null
                    binding.tilIdMobileSign.isErrorEnabled = false

                    dialog?.hide()

                    val studentItemsM = snapshot.child("token").value
                    Log.i("main", "token: $studentItemsM")

                    if (enteredMobile == studentItemsM) {
                        binding.tilMobileMobileSign.error = null
                        binding.tilMobileMobileSign.isErrorEnabled = false

                        dialog?.dismiss()

                        val intent = Intent(this@MobileSignIn, MobileSignIn2::class.java).apply {
                            putExtra("batch", batch)
                            putExtra("id", snapshot.key)
                            putExtra("mobile", enteredMobile)
                        }
                        startActivity(intent)

                    } else {
                        dialog?.dismiss()
                        binding.tilMobileMobileSign.error = "Mobile number not match"
                        binding.tilMobileMobileSign.requestFocus()
                    }

                }
                // if snap not exist
                else {
                    dialog?.dismiss()
                    Log.i("main", "No Snapshot Found")
                    binding.tilIdMobileSign.error = "Student Id not found"
                    binding.tilIdMobileSign.requestFocus()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MobileSignIn,
                    "Database error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                dialog?.dismiss()
            }

        })

    }

    //**********************************************************************************************//
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
        (binding.acBatchMobileSign as AutoCompleteTextView?)?.setAdapter(adapterBatch)
        (binding.acBatchMobileSign as AutoCompleteTextView?)?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->

                when (position) {
                    0 -> {
                        binding.acIdMobileSign.setText("")
                        binding.tilIdMobileSign.visibility = View.VISIBLE
                        (binding.acIdMobileSign as AutoCompleteTextView?)?.setAdapter(adapterBatch15)
                    }

                    1 -> {
                        binding.acIdMobileSign.setText("")
                        binding.tilIdMobileSign.visibility = View.VISIBLE
                        (binding.acIdMobileSign as AutoCompleteTextView?)?.setAdapter(adapterBatch14)
                    }

                    2 -> {
                        binding.acIdMobileSign.setText("")
                        binding.tilIdMobileSign.visibility = View.VISIBLE
                        (binding.acIdMobileSign as AutoCompleteTextView?)?.setAdapter(adapterBatch13)
                    }

                    3 -> {
                        binding.acIdMobileSign.setText("")
                        binding.tilIdMobileSign.visibility = View.VISIBLE
                        (binding.acIdMobileSign as AutoCompleteTextView?)?.setAdapter(adapterBatch12)
                    }

                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun loadBirthDate() {
        binding.tilBirthMobileSign3.visibility = View.VISIBLE

        val mCurrentTime = Calendar.getInstance()
        val year = mCurrentTime.get(Calendar.YEAR)
        val month = mCurrentTime.get(Calendar.MONTH)
        val day = mCurrentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            { view, yearF, monthF, dayF ->
                binding.tvBirthMobileSign3.setText(
                    "$dayF-${monthF + 1}-$yearF"
                )
            }, year, month, day
        )
        datePicker.show()


    }

    // autoComplete blood
    private fun loadBloodGr() {
        //list
        val bloodGrList = listOf(
            "A+", "B+", "AB+", "O+",
            "A-", "B-", "AB-", "O-"
        )
        //array adapter
        val adapterBlood = ArrayAdapter(this, R.layout.material_spinner_item, bloodGrList)
        (binding.acBloodMobileSign3 as AutoCompleteTextView?)?.setAdapter(adapterBlood)
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
        (binding.acHallMobileSign3 as AutoCompleteTextView?)?.setAdapter(adapterHall)
    }

    //**********************************************************************************************//
    // student id
    private fun validateStudentBatch(): Boolean {
        val value = binding.acBatchMobileSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilBatchMobileSign.error = "Field can not be empty"
                false
            }
            else -> {
                binding.tilBatchMobileSign.error = null
                binding.tilBatchMobileSign.isErrorEnabled = false
                true
            }
        }
    }

    // student id
    private fun validateStudentId(): Boolean {
        val value = binding.acIdMobileSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilIdMobileSign.error = "Field can not be empty"
                false
            }
            value.length < 8 -> {
                binding.tilIdMobileSign.error = ("Student Id should contain 8 digits!")
                false
            }
            else -> {
                binding.tilIdMobileSign.error = null
                binding.tilIdMobileSign.isErrorEnabled = false
                true
            }
        }
    }

    // student mobile no
    private fun validateStudentMobile(): Boolean {
        val value = binding.etMobileMobileSign.text.toString()

        return when {
            value.isEmpty() -> {
                binding.tilMobileMobileSign.error = "Field can not be empty"
                false
            }
            value.length < 11 -> {
                binding.tilMobileMobileSign.error = ("Password should contain 11 digits!")
                false
            }
            else -> {
                binding.tilMobileMobileSign.error = null
                binding.tilMobileMobileSign.isErrorEnabled = false
                true
            }
        }
    }
}