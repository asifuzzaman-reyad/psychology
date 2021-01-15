package com.reyad.psychology.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.R
import com.reyad.psychology.databinding.FragmentStudy2Binding

class FragmentStudy : Fragment() {

    private lateinit var _binding: FragmentStudy2Binding
    private val binding get() = _binding

    private var batch: String? = null
    private var year: String? = null
    private lateinit var courseConst: ConstraintLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var code1: TextView
    private lateinit var code2: TextView
    private lateinit var code3: TextView
    private lateinit var code4: TextView
    private lateinit var code5: TextView
    private lateinit var code6: TextView
    private lateinit var yearTv: TextView

    private lateinit var name1: TextView
    private lateinit var name2: TextView
    private lateinit var name3: TextView
    private lateinit var name4: TextView
    private lateinit var name5: TextView
    private lateinit var name6: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStudy2Binding.inflate(inflater, container, false)
        val view = binding.root

        //hook
        courseConst = view.findViewById(R.id.const_course_study)
        progressBar = view.findViewById(R.id.progressBar_study)

        // firebase auth -> on start
        retrieveFirebaseAuth()

        // item click listener
        courseOnClick()

        return view
    }


    // retrieve auth data
    private fun retrieveFirebaseAuth() {
        //progress
        progressBar.visibility = View.VISIBLE

        //auth
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        //database
        val db = FirebaseDatabase.getInstance().reference
        val userRef = db.child("Users").child(uid!!)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                    batch = snapshot.child("batch").value.toString()
                    Log.i("main", "$batch ")

                    // set course code and name
                    setAllCourse(view!!)

                    courseConst.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }

        })
    }

    // all courses logic
    @SuppressLint("SetTextI18n")
    private fun setAllCourse(view: View) {
        //hooks
        code1 = view.findViewById(R.id.tv_study_code1)
        code2 = view.findViewById(R.id.tv_study_code2)
        code3 = view.findViewById(R.id.tv_study_code3)
        code4 = view.findViewById(R.id.tv_study_code4)
        code5 = view.findViewById(R.id.tv_study_code5)
        code6 = view.findViewById(R.id.tv_study_code6)
        yearTv = view.findViewById(R.id.tv_study_batch)

        name1 = view.findViewById(R.id.tv_study_name1)
        name2 = view.findViewById(R.id.tv_study_name2)
        name3 = view.findViewById(R.id.tv_study_name3)
        name4 = view.findViewById(R.id.tv_study_name4)
        name5 = view.findViewById(R.id.tv_study_name5)
        name6 = view.findViewById(R.id.tv_study_name6)

        // assign batch -> year
        when (batch) {
            "Batch 15" -> {
                year = "1st Year"
            }
            "Batch 14" -> {
                year = "2nd Year"
            }
            "Batch 13" -> {
                year = "3rd Year"
            }
            "Batch 12" -> {
                year = "4th Year"
            }
        }

        // assign year -> course
        when (year) {
            "1st Year" -> {
                code1.text = "Psy 101"
                code2.text = "Psy 102"
                code3.text = "Psy 103"
                code4.text = "Psy 104"
                code5.text = "Psy 105"
                code6.text = "Psy 106"
                yearTv.text = "1st Year"

                name1.text = "General Psychology"
                name2.text = "Social Psychology"
                name3.text = "Experimental Psychology"
                name4.text = "Sociology"
                name5.text = "Psychological Statistic i"
                name6.text = "Fundamentals of Computer"
            }

            "2nd Year" -> {
                code1.text = "Psy 201"
                code2.text = "Psy 202"
                code3.text = "Psy 203"
                code4.text = "Psy 204"
                code5.text = "Psy 205"
                code6.text = "Psy 206"
                yearTv.text = "2nd Year"

                name1.text = "Developmental Psychology"
                name2.text = "Educational Psychology"
                name3.text = "Behavioral NeuroScience"
                name4.text = "Cultural Anthropology"
                name5.text = "Psychological Statistic ii"
                name6.text = "Computer Applications"
            }

            "3rd Year" -> {
                code1.text = "Psy 301"
                code2.text = "Psy 302"
                code3.text = "Psy 303"
                code4.text = "Psy 304"
                code5.text = "Psy 305"
                code6.text = "Psy 306"
//                code7.text = "Psy 307"
                yearTv.text = "3rd Year"

                name1.text = "Psychological Testing"
                name2.text = "Research Methodology"
                name3.text = "Abnormal Psychology"
                name4.text = "Industrial Psychology"
                name5.text = "Counseling Psychology"
                name6.text = "Health Psychology"
//                name7.text = "Psychology of Crime"
            }

            "4th Year" -> {
                code1.text = "Psy 401"
                code2.text = "Psy 402"
                code3.text = "Psy 403"
                code4.text = "Psy 404"
                code5.text = "Psy 405"
                code6.text = "Psy 406"
//                code7.text = "Psy 407"
//                code8.text = "Psy 408"
                yearTv.text = "4th Year"

                name1.text = "Psychology of Learning"
                name2.text = "Personality Development"
                name3.text = "Theories of Perception"
                name4.text = "Developmental Psy. ii"
                name5.text = "Organizational Behavior"
                name6.text = "Clinical Psychology"
//                name7.text = "Cognitive Psychology"
//                name8.text = "Positive Psychology"
            }

        }
    }

    // course click listener
    private fun courseOnClick() {
        binding.ln01.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStudy2_to_courseList)
            sendStudySharedPref(code1.text.toString ())
            Log.i("study1", "code:${code1.text}")
        }

        binding.ln02.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStudy2_to_courseList)
            sendStudySharedPref(code2.text.toString ())
        }

        binding.ln03.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStudy2_to_courseList)
            sendStudySharedPref(code3.text.toString ())
        }

        binding.ln04.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStudy2_to_courseList)
            sendStudySharedPref(code4.text.toString ())
        }

        binding.ln05.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStudy2_to_courseList)
            sendStudySharedPref(code5.text.toString ())
        }
        binding.ln06.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStudy2_to_courseList)
            sendStudySharedPref(code6.text.toString ())
        }
    }

    //
    @SuppressLint("CommitPrefEdits")
    fun sendStudySharedPref(
        value1: String
    ) {
        val sharedPreferences = context?.getSharedPreferences("study", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("key1", value1)
        editor?.apply()
    }
}