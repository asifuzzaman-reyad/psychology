package com.reyad.psychology.register.user

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityProfileBinding
import com.reyad.psychology.register.login.FirstActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var mAuth: FirebaseAuth

    private var batch: String? = null
    private var id: String? = null

    private var name: String? = null
    private var hall: String? = null
    private var mobile: String? = null
    private var blood: String? = null
    private var imageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //sign out
        binding.signOutProfile.setOnClickListener {
            // sign out alert dialog
            signOutAlertDialog()
        }

        // go to edit profile
        binding.btnEditProfile.setOnClickListener {
            goToEditProfileIntent()
        }

    }

    //
    override fun onStart() {
        super.onStart()
        // firebase auth
        loadUserInfo()
    }

    // firebase auth
    private fun loadUserInfo() {

        batch = intent.getStringExtra("batch").toString()
        id = intent.getStringExtra("id").toString()


        val db = FirebaseDatabase.getInstance().getReference("Students")
        val ref = db.child(batch!!).child(id!!)

        //firebase offline
        ref.keepSynced(true)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.i("testProfile", snapshot.child("name").value.toString())

                    name = snapshot.child("name").value.toString()
                    hall = snapshot.child("hall").value.toString()
                    mobile = snapshot.child("mobile").value.toString()
                    blood = snapshot.child("blood").value.toString()
                    imageUrl = snapshot.child("imageUrl").value.toString()

                    Log.i("profile", "$batch ->> $id ->> $name->> $hall ->> $mobile ->> $imageUrl")

                    //
                    binding.tvNameProfile.text = name
                    binding.tvIdProfile.text = id
                    binding.tvHallProfile.text = hall
                    binding.tvMobileProfile.text = mobile
                    binding.tvBloodProfile.text = blood

                    //picasso offline
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


                } else {
                    Toast.makeText(this@Profile, "Something wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //
    private fun signOutAlertDialog() {
        val title = "Sign Out"
        val message = "Do you want to Sign Out?"

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                // sendToFirstActivity
                goToFirstActivity()
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
    private fun goToFirstActivity() {
        FirebaseAuth.getInstance().signOut()
        val mainIntent = Intent(this, FirstActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    // go to editProfile
    private fun goToEditProfileIntent() {
        val editProfileIntent = Intent(this, ProfileEdit::class.java).apply {
            putExtra("batch", batch.toString())
            putExtra("id", id.toString())
            putExtra("name", name.toString())
            putExtra("blood", blood.toString())
            putExtra("hall", hall.toString())
            putExtra("mobile", mobile.toString())
            putExtra("imageUrl", imageUrl.toString())
        }

        startActivity(editProfileIntent)
    }

}