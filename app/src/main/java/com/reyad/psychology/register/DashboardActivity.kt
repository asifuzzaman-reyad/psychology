package com.reyad.psychology.register

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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

    // firebase auth
    private fun retrieveFirebaseUsers() {
        name = intent.getStringExtra("name").toString()
        id = intent.getStringExtra("id").toString()
        hall = intent.getStringExtra("hall").toString()
        imageUrl = intent.getStringExtra("imageUrl")

        binding.tvNameProfile.text = name
        binding.tvIdProfile.text = id
        binding.tvHallProfile.text = hall

        if (imageUrl!!.isNotEmpty()) {
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

        //load bar code
        barcode(id.toString())
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
    private fun barcode(id: String) {
        val multiFormatWriter = MultiFormatWriter()

        val imageView = binding.idBarcode
        try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode(id, BarcodeFormat.CODE_128, 200, 50)
            val barCodeEncoder = BarcodeEncoder()
            val bitmap = barCodeEncoder.createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}