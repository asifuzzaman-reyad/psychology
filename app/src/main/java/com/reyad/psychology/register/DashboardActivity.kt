package com.reyad.psychology.register

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityProfileBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var mAuth: FirebaseAuth

    private var batch: String? = null
    private var id: String? = null
    private var name: String? = null
    private var hall: String? = null
    private var mobile: String? = null
    private var imageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // firebase auth
        retrieveFirebaseUsers()

        //load bar code
        barcode()

        //sign out
        binding.signOutProfile.setOnClickListener {
            // sign out alert dialog
            alertDialog()
        }

        // go to edit profile
        binding.btnEditProfile.setOnClickListener {
            editProfileIntent()
        }

    }

    //
    private fun alertDialog() {
        val title = "Sign Out"
        val message = "Do you want to Sign Out?"

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                // sendToFirstActivity
                sendToFirstActivity()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }

    // firebase auth
    private fun retrieveFirebaseUsers() {
        // hook auth
        mAuth = FirebaseAuth.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // class
        class UserItems(
            val batch: String? = "",
            val id: String? = "",
        ) {
            constructor() : this("", "")
        }

        //database
        val db = FirebaseDatabase.getInstance().reference
        val userRef = db.child("Users_Mobile").child(uid!!)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userData = snapshot.getValue(UserItems::class.java)

                batch = userData?.batch.toString()
                id = userData?.id.toString()
                Log.i("profileActivity", "$batch ->> $id")

                // retrieve firebase data
                getStudentRef(batch!!, id!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // student data retrieve
    private fun getStudentRef(batch: String, id: String) {

        val mStudentDatabase = FirebaseDatabase.getInstance().reference
            .child("Students").child(batch).child(id)

        // firebase offline
        mStudentDatabase.keepSynced(false)

        //
        class StudentItems(
            val name: String? = "",
            val hall: String? = "",
            val mobile: String? = "",
            val imageUrl: String? = "",
        ) {
            constructor() : this("", "", "", "")
        }

        mStudentDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val studentData = snapshot.getValue(StudentItems::class.java)

                    imageUrl = studentData?.imageUrl.toString()
                    if (imageUrl!!.isNotEmpty()) {
//                        Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar).into(binding.profileImgProfile)
                        Picasso.get().load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.male_avatar)
                            .into(binding.profileImgProfile, object : Callback {
                                override fun onSuccess() {


                                }

                                override fun onError(e: java.lang.Exception?) {

                                    Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
                                        .into(binding.profileImgProfile)

                                }

                            })
                    }

                    name = studentData?.name.toString()
                    hall = studentData?.hall.toString()
                    mobile = studentData?.mobile.toString()

                    binding.tvNameProfile.text = name
                    binding.tvIdProfile.text = id
                    binding.tvHallProfile.text = hall
//                    binding.tvHallProfile.text = mobile
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // send to start
    private fun sendToFirstActivity() {
        FirebaseAuth.getInstance().signOut()
        val mainIntent = Intent(this, FirstActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    // go to editProfile
    private fun editProfileIntent() {
        val editProfileIntent = Intent(this, ProfileEdit::class.java).apply {
            putExtra("batch", batch.toString())
            putExtra("id", id.toString())
            putExtra("name", name.toString())
            putExtra("hall", hall.toString())
            putExtra("mobile", mobile.toString())
            putExtra("imageUrl", imageUrl.toString())
        }
        startActivity(editProfileIntent)
    }

    //bar code
    private fun barcode() {
        val multiFormatWriter = MultiFormatWriter()

        val imageView = binding.idBarcode
        try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode("18608047", BarcodeFormat.CODE_128, 200, 50)
            val barCodeEncoder = BarcodeEncoder()
            val bitmap = barCodeEncoder.createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}