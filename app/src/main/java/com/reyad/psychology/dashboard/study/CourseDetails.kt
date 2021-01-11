package com.reyad.psychology.dashboard.study

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentCourseDetailsBinding

class CourseDetails : Fragment() {

    private lateinit var _binding: FragmentCourseDetailsBinding
    private val binding get() = _binding

    private var getCode: String? = null
    private var getTopic: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        //
        getCourseListSharedPref()

        //
        retrieveFirebaseData(getCode.toString(), getTopic.toString())

        return view
    }

    // get courseList fragment data
    private fun getCourseListSharedPref() {
        val sharedPreferences = context?.getSharedPreferences("details", Context.MODE_PRIVATE)
        getCode = sharedPreferences?.getString("key1", "code")
        getTopic = sharedPreferences?.getString("key2", "pdfLink")
        Log.i("courseDetails", "courseDetails: ${getCode.toString()} ->> ${getTopic.toString()}")
    }

    //
    private fun retrieveFirebaseData(code: String, topic: String) {
        val db = FirebaseDatabase.getInstance().reference
        val ref = db.child("Resources")
            .child(code)
            .child(topic)

        val mItems = ArrayList<PdfItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data = it.getValue(PdfItemList::class.java)
                    mItems.add(data!!)
                    Log.i("pdfDetails", it.toString())

                    val adapter = PdfDetailsAdapter(requireContext(), mItems)

                    binding.recyclerViewCourseDetails.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Resource Data Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}