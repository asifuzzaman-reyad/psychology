@file:Suppress("DEPRECATION")

package com.reyad.psychology.register

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.reyad.psychology.databinding.ActivityProfileEditBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.google.firebase.storage.StorageReference
import com.reyad.psychology.R
import com.squareup.picasso.Picasso


private const val GALLERY_PICK = 111

class ProfileEdit : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorageRef: StorageReference
    private lateinit var progressDialog: ProgressDialog

    private var batch: String? = null
    private var id: String? = null
    private var name: String? = null
    private var hall: String? = null
    private var mobile: String? = null
    private var imageUrl: String? = null

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // hooks
        mAuth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().reference
        progressDialog = ProgressDialog(this)

        //get Extra data
        batch = intent.getStringExtra("batch").toString()
        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("name").toString()
        hall = intent.getStringExtra("hall").toString()
        mobile = intent.getStringExtra("mobile").toString()
        imageUrl = intent.getStringExtra("imageUrl").toString()
        Log.i("profileEdit", "$batch ->> $id ->> $name->> $hall ->> $imageUrl")

        //
        if (imageUrl!!.isNotEmpty()) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar).into(binding.circularEditProfile)
        }
        binding.tvNameEditProfile.setText(name)
        binding.tvMobileEditProfile.setText(mobile)

        // pick image
        binding.circularEditProfile.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(galleryIntent, "SELECT IMAGE"),
                GALLERY_PICK
            )
        }

        // update
        binding.btnUpdateEditProfile.setOnClickListener {
            studentInfoUpdateToFirebase()
        }
    }

    // image request
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            //get image from gallery
            val imageUrl: Uri? = data?.data

            // crop selected image
            CropImage.activity(imageUrl)
                .setAspectRatio(1, 1)
                .start(this)
        }

        // override selected image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {

                selectedImageUri = result.uri
                Picasso.get().load(selectedImageUri).into(binding.circularEditProfile)

                //image update
                uploadImage()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("editProfile", "crop error: $error")
            }
        }
    }

    // upload image to storage
    private fun uploadImage() {
        //progress
        progressDialog.setTitle("Uploading Image")
        progressDialog.setMessage("Please wait.\nProcess and upload the image...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        //put file to storage
        val db = FirebaseStorage.getInstance().reference
        val ref = db.child("Students")
            .child(batch.toString())
            .child(id.toString())

        // get download url
        selectedImageUri?.let { it ->
            ref.putFile(it)
                .addOnSuccessListener { it ->
                    Log.d("editProfile", "Image upload on storage")

                    ref.downloadUrl.addOnSuccessListener { uri ->
                        downloadUrlUpdateToFirebase(uri.toString())
                        Log.d("editProfile", "Image url: $uri")
                    }
                }
                .addOnFailureListener {
                    Log.i("editProfile", "Storage failed: ${it.message.toString()}")
                    Toast.makeText(
                        this,
                        "Storage failed: ${it.message.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    // update image utl -> Firebase
    private fun downloadUrlUpdateToFirebase(uri: String) {
        val imageHash: MutableMap<String, Any> = HashMap()
        imageHash["imageUrl"] = uri

        // firebase ref
        val db = FirebaseDatabase.getInstance().reference
        db.child("Students")
            .child(batch.toString())
            .child(id.toString())
            .updateChildren(imageHash).addOnCompleteListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this, "Image Upload: successfully", Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                progressDialog.hide()
                Log.i("editProfile", "Storage failed: ${it.message.toString()}")
            }
    }

    // update student info -> Firebase
    private fun studentInfoUpdateToFirebase() {
        //progress
        progressDialog.setTitle("Updating Profile")
        progressDialog.setMessage("Please wait.\nProfile information updating")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        val imageHash: MutableMap<String, Any> = HashMap()
        imageHash["name"] = binding.tvNameEditProfile.text.toString()
        imageHash["mobile"] = binding.tvMobileEditProfile.text.toString()

        // firebase ref
        val db = FirebaseDatabase.getInstance().reference
        db.child("Students")
            .child(batch.toString())
            .child(id.toString())
            .updateChildren(imageHash).addOnCompleteListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this, "Profile Update: successfully", Toast.LENGTH_SHORT
                ).show()

                // go back to profile
                val profileIntent = Intent(this, DashboardActivity::class.java)
                startActivity(profileIntent)
                finish()
            }
            .addOnFailureListener {
                progressDialog.hide()
                Log.i("editProfile", "Profile Update: ${it.message.toString()}")
            }
    }
}

