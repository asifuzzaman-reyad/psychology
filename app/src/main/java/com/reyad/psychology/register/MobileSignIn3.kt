package com.reyad.psychology.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityMobileSignIn3Binding
import java.util.*

class MobileSignIn3 : AppCompatActivity() {
    private lateinit var binding: ActivityMobileSignIn3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileSignIn3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // load hall
        loadBloodGr()
        loadHall()

        //
        binding.btnBirthMobileSign3.setOnClickListener { loadBirthDate() }

        //
        binding.btnSignUpMobileSign3.setOnClickListener {

        }
    }

    //
    @SuppressLint("SetTextI18n")
    private fun loadBirthDate() {
        binding.tilBirthMobileSign3.visibility = View.VISIBLE

        val mCurrentTime = Calendar.getInstance()
        val year = mCurrentTime.get(Calendar.YEAR)
        val month = mCurrentTime.get(Calendar.MONTH)
        val day = mCurrentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            { view, yearF, monthF, dayF ->
                binding.tvBirthMobileSign3.setText(
                    "$dayF-${monthF+1}-$yearF"
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
}