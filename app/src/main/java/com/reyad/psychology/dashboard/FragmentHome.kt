package com.reyad.psychology.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.dashboard.home.student.BatchCurrent
import com.reyad.psychology.dashboard.home.student.StudentMain
import com.reyad.psychology.dashboard.home.teacher.TeacherMain
import com.reyad.psychology.databinding.FragmentHomeBinding
import com.reyad.psychology.messenger.Messenger
import com.reyad.psychology.register.user.Profile
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

private const val TAG = "fragmentHome"

class FragmentHome : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var dialog: SpotsDialog

    private var batch: String? = null
    private var id: String? = null
    private var name: String? = null
    private var hall: String? = null
    private var mobileNo: String? = null
    private var imageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        retrieveFirebaseAuth()

        // button profile
        binding.civProfileHome.setOnClickListener {

            val profileIntent = Intent(requireContext(), Profile::class.java).apply {
                putExtra("batch", batch.toString())
                putExtra("id", id.toString())
            }
            startActivity(profileIntent)
        }

        // button teacher
        binding.btnTeacherHome.setOnClickListener {
            val teacherMainIntent = Intent(requireContext(), TeacherMain::class.java)
            startActivity(teacherMainIntent)
        }

        // button student
        binding.btnStudentHome.setOnClickListener {
            val intentBatchCurrent = Intent(requireContext(), BatchCurrent::class.java).apply {
                putExtra("batch", batch)
            }
            startActivity(intentBatchCurrent)
        }

        // button teacher
        binding.btnRoutineHome.setOnClickListener {
            Toast.makeText(context, "Update coming soon...", Toast.LENGTH_SHORT).show()
        }

        // button messenger
        binding.btnMessengerHome.setOnClickListener {
            val messengerIntent = Intent(requireContext(), Messenger::class.java).apply {
                putExtra("batch", batch)
                putExtra("id", id)
                putExtra("name", name)
                putExtra("imageUrl", imageUrl)
            }
            startActivity(messengerIntent)
        }



        return view
    }

    // retrieve auth data
    private fun retrieveFirebaseAuth() {
        //
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        //database
        val db = FirebaseDatabase.getInstance().reference
        val userRef = db.child("Users").child(uid!!)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                batch = snapshot.child("batch").value.toString()
                id = snapshot.child("id").value.toString()

                Log.i(TAG, "batch ---> $batch")
                Log.i(TAG, "id ---> $id")

                // get student data
                getStudentRef(batch!!, id!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // student
    private fun getStudentRef(batch: String, id: String) {
        val db = FirebaseDatabase.getInstance().reference
        val studentRef = db.child("Students")
            .child(batch)
            .child(id)

        studentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                name = snapshot.child("name").value.toString()
                hall = snapshot.child("hall").value.toString()
                mobileNo = snapshot.child("mobile").value.toString()
                imageUrl = snapshot.child("imageUrl").value.toString()

                Log.i(TAG, "hall: ${hall.toString()}")
                Log.i(TAG, "imageUrl: ${imageUrl.toString()}")

                //
                if (imageUrl!!.isNotEmpty()) {
                    Picasso.get().load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.male_avatar)
                        .into(binding.civProfileHome, object : Callback {
                            override fun onSuccess() {
                            }

                            override fun onError(e: java.lang.Exception?) {
                                Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
                                    .into(binding.civProfileHome)
                            }
                        })
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

}