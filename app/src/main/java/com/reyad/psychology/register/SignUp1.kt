package com.reyad.psychology.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivitySignUp1Binding
import com.reyad.psychology.register.login.SignUp2

class SignUp1 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp1Binding
    private lateinit var nextBtn: MaterialButton
    private lateinit var alreadyBtn: MaterialButton
    private lateinit var tokenTv: EditText
    private lateinit var batchAutoCom: EditText
    private lateinit var idAutoCom: EditText
    private lateinit var idLayout: TextInputLayout
    private lateinit var hallAutoCom: EditText
    private lateinit var mobileTv: EditText
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //hook button
        nextBtn = findViewById(R.id.btn_sign_up1_next)
        alreadyBtn = findViewById(R.id.btn_sign_up1_already)
        tokenTv = findViewById(R.id.et_sign_up1_token)
        batchAutoCom = findViewById(R.id.ac_sign_up1_batch)
        idAutoCom = findViewById(R.id.ac_sign_up1_id)
        idLayout = findViewById(R.id.til_sign_Up1_id)
        hallAutoCom = findViewById(R.id.ac_sign_up1_hall)
        mobileTv = findViewById(R.id.et_sign_up1_mobile)
        progressDialog = ProgressDialog(this)

        // load autocomplete
        loadBatchAndId()
        loadHall()

        //button already
        alreadyBtn.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
//            finish()
        }

        //button next
        nextBtn.setOnClickListener {
            // init value
            val batch = batchAutoCom.text.toString()
            val id = idAutoCom.text.toString()
            val token = tokenTv.text.toString()
            val hall = hallAutoCom.text.toString()
            val mobile = mobileTv.text.toString()

            if (TextUtils.isEmpty(batch) or TextUtils.isEmpty(id) or TextUtils.isEmpty(token)) {
                Toast.makeText(
                    this,
                    "Enter correct batch, id & token to continue",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // check edit text
                checkBatchAndId(batch, id, token, hall, mobile)
            }

//                goToSignUp2(token, batch, id, hall, mobile)

        }

    }

    //check batch and id
    private fun checkBatchAndId(
        batch: String, id: String, token: String, hall: String, mobile: String,
    ) {
//        progress
        progressDialog.setTitle("Searching...")
        progressDialog.setMessage("Find User token, Student Id on Database")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        // firebase
        val db = FirebaseDatabase.getInstance().reference
        val ref = db.child("Tokens").child(batch)

        // class
        class StudentItem(val token: String? = "") {
            constructor() : this("")
        }

        //ref
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val studentItem = it.getValue(StudentItem::class.java)

                    if (it.key == id) {
                        binding.tilSignUp1Id.error = null
                    } else {
                        progressDialog.hide()
                        binding.tilSignUp1Id.error = "Check student id"
                    }

                    if (studentItem!!.token == token) {
                        binding.tilSignUp1Token.error = null

                    } else {
                        progressDialog.hide()
                        binding.tilSignUp1Token.error = "Check user token"
                    }

                    if (it.key == id && studentItem.token == token) {
                        goToSignUp2(batch, id, token, hall, mobile)
                        progressDialog.dismiss()
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.hide()
                Log.i("signUp10", "Not found:")
            }

        })

    }

    //   goToSignUp2
    private fun goToSignUp2(
        batch: String, id: String, token: String, hall: String, mobile: String
    ) {
        val signUp2Intent = Intent(applicationContext, SignUp2::class.java).apply {
            putExtra("batch", batch)
            putExtra("id", id)
            putExtra("hall", hall)
            putExtra("mobile", mobile)
        }
        startActivity(signUp2Intent)
    }

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
        (batchAutoCom as AutoCompleteTextView?)?.setAdapter(adapterBatch)
        (batchAutoCom as AutoCompleteTextView?)?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->

                when (position) {
                    0 -> {
                        idAutoCom.setText("")
                        idLayout.visibility = View.VISIBLE
                        (idAutoCom as AutoCompleteTextView?)?.setAdapter(adapterBatch15)
                    }

                    1 -> {
                        idAutoCom.setText("")
                        idLayout.visibility = View.VISIBLE
                        (idAutoCom as AutoCompleteTextView?)?.setAdapter(adapterBatch14)
                    }

                    2 -> {
                        idAutoCom.setText("")
                        idLayout.visibility = View.VISIBLE
                        (idAutoCom as AutoCompleteTextView?)?.setAdapter(adapterBatch13)
                    }

                    3 -> {
                        idAutoCom.setText("")
                        idLayout.visibility = View.VISIBLE
                        (idAutoCom as AutoCompleteTextView?)?.setAdapter(adapterBatch12)
                    }

                }
            }
    }

    // autoComplete hall
    private fun loadHall() {
        //list
        val hallList = listOf(
            "Shaheed Abdur Rab Hall", "Pritilata Hall", "Shamsun Nahar Hall",
            "Deshnetri Begum Khaleda Zia Hall", "Bangamata Sheikh Fazilatunnesa Mujib Hall",
            "Jononetri Sheikh Hasina Hall"
        )
        //array adapter
        val adapterHall = ArrayAdapter(this, R.layout.material_spinner_item, hallList)
        (hallAutoCom as AutoCompleteTextView?)?.setAdapter(adapterHall)
    }

}