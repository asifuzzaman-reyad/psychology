package com.reyad.psychology.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.dashboard.home.student.StudentMain
import com.reyad.psychology.databinding.FragmentHomeBinding
import com.reyad.psychology.messenger.Messenger
import com.reyad.psychology.register.DashboardActivity
import com.squareup.picasso.Picasso

private const val TAG = "fragmentHome"

class FragmentHome : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private var batch: String? = null
    private var id: String? = null
    private var name: String? = null
    private var hall: String? = null
    private var imageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // button profile
        binding.civProfileHome.setOnClickListener {
            val profileIntent = Intent(requireContext(), DashboardActivity::class.java).apply {
                putExtra("batch", batch.toString())
                putExtra("id", id.toString())
                putExtra("name", name.toString())
                putExtra("hall", hall.toString())
                putExtra("imageUrl", imageUrl.toString())
            }
            startActivity(profileIntent)
        }

        // button student
        binding.btnStudentHome.setOnClickListener {
            val studentIntent = Intent(requireContext(), StudentMain::class.java)
            startActivity(studentIntent)
        }

        // button messenger
        binding.btnMessengerHome.setOnClickListener {
            val messengerIntent = Intent(requireContext(), Messenger::class.java)
            startActivity(messengerIntent)
        }

        // button teacher
        binding.btnTeacherHome.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_teacherMain)
        }

        return view
    }

    // firebase auth -> on start
    override fun onStart() {
        super.onStart()
        retrieveFirebaseAuth()
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
                getStudentRef(batch!!, id!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
                imageUrl = snapshot.child("imageUrl").value.toString()
                Log.i(TAG, "hall: ${hall.toString()}")
                Log.i(TAG, "imageUrl: ${imageUrl.toString()}")

                if (imageUrl!!.isNotEmpty()) {
                    Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
                        .into(binding.civProfileHome)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}