package com.reyad.psychology.dashboard.study

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.reyad.psychology.R
import com.reyad.psychology.databinding.FragmentCourseListBinding

class CourseList : Fragment() {

    private lateinit var _binding: FragmentCourseListBinding
    private val binding get() = _binding

    private var courseCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        val view = binding.root

        //
        getStudySharedPref()

        // course list on click
        courseListOnClick()

        return view
    }

    private fun courseListOnClick() {
        binding.tvSyllabusCourseList.setOnClickListener {
            findNavController().navigate(R.id.action_courseList_to_courseDetails)
            sendCourseListSharedPref(courseCode.toString(), "syllabus")
        }
        binding.tvQuestionsCourseList.setOnClickListener {
            findNavController().navigate(R.id.action_courseList_to_courseDetails)
            sendCourseListSharedPref(courseCode.toString(), "questions")
        }
        binding.tvNotesCourseList.setOnClickListener { }
        binding.tvBooksCourseList.setOnClickListener { }
        binding.tvTeacher1CourseList.setOnClickListener { }
        binding.tvTeacher2CourseList.setOnClickListener { }
    }

    // get study fragment data
    @SuppressLint("CommitPrefEdits")
    fun getStudySharedPref() {
        val sharedPreferences = context?.getSharedPreferences("study", Context.MODE_PRIVATE)
        val getCode = sharedPreferences?.getString("key1", "")

        courseCode = getCode.toString()
        Log.i("courseList", "code:${getCode.toString()}")
    }

    // send course list data
    @SuppressLint("CommitPrefEdits")
    fun sendCourseListSharedPref(
        value1: String, value2: String
    ) {
        val sharedPreferences = context?.getSharedPreferences("details", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("key1", value1)
        editor?.putString("key2", value2)
        editor?.apply()
    }

}