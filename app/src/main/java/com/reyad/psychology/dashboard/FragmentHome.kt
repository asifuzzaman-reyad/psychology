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
import com.reyad.psychology.register.ProfileActivity
import com.squareup.picasso.Picasso

class FragmentHome : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private var batch: String? = null
    private var id: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // button profile
        binding.civProfileHome.setOnClickListener {
            val profileIntent = Intent(requireContext(), ProfileActivity::class.java).apply {
                putExtra("batch", batch.toString())
                putExtra("id", id.toString())
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

        // class
        class UserItems(
            val batch: String? = "",
            val id: String? = "",
        ) {
            constructor() : this("", "")
        }

        //database
        val db = FirebaseDatabase.getInstance().reference
        val userRef = db.child("Users").child(uid!!)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val userData = snapshot.getValue(UserItems::class.java)

                    batch = userData?.batch.toString()
                    id = userData?.id.toString()

                    Log.i("main", "$batch ---> $id")
                    getStudentRef(batch!!, id!!)
                }
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

        //
        class StudentItems(
            val name: String? = "",
            val imageUrl: String? = "",
        ) {
            constructor() : this("", "")
        }

        studentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val studentData = snapshot.getValue(StudentItems::class.java)

                    val imageUrl = studentData?.imageUrl.toString()
                    Log.i("mainImage", imageUrl)

                    if (imageUrl.isNotEmpty()) {
                        Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
                            .into(binding.civProfileHome)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}