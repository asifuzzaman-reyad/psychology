package com.reyad.psychology.dashboard.study.allCourse

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentCourseCategoryBinding

class CourseCategory : Fragment() {

    private lateinit var _binding: FragmentCourseCategoryBinding
    private val binding get() = _binding

    private var getYear: String? = null
    private var getCourseCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        //
        getSharedPrefStudy()
        val tvCategory = "Course Category ($getCourseCode)"
        binding.tvHeaderCourseCategory.text = tvCategory

        // course list on click
        courseListOnClick()

        return view
    }

    //
    private fun courseListOnClick() {

        binding.tvSyllabusCourseList.setOnClickListener {
            val intent = Intent(context, CourseChapterLesson::class.java).apply {
                putExtra("year", getYear)
                putExtra("courseCode", getCourseCode)
                putExtra("category", "Syllabus")
            }
            startActivity(intent)
        }
        binding.tvQuestionsCourseList.setOnClickListener {
            val intent = Intent(context, CourseChapterLesson::class.java).apply {
                putExtra("year", getYear)
                putExtra("courseCode", getCourseCode)
                putExtra("category", "Questions")
            }
            startActivity(intent)

        }
        binding.tvNotesCourseList.setOnClickListener {
            val intent = Intent(context, CourseChapter::class.java).apply {
                putExtra("year", getYear)
                putExtra("courseCode", getCourseCode)
                putExtra("category", "Notes")
            }
            startActivity(intent)
        }
        binding.tvBooksCourseList.setOnClickListener {
            val intent = Intent(context, CourseChapterLesson::class.java).apply {
                putExtra("year", getYear)
                putExtra("courseCode", getCourseCode)
                putExtra("category", "Books")
            }
            startActivity(intent)
        }

    }

    // get studyFragment data
    @SuppressLint("CommitPrefEdits")
    fun getSharedPrefStudy() {
        val sharedPreferences = context?.getSharedPreferences("studyFragment", Context.MODE_PRIVATE)
        getYear = sharedPreferences?.getString("year", "")
        getCourseCode = sharedPreferences?.getString("courseCode", "")

        Log.i("courseList", "year:${getYear.toString()} ,code:${getCourseCode.toString()}")
    }

    // send course list data
    @SuppressLint("CommitPrefEdits")
    fun sendSharedPrefCourseList(
        value1: String, value2: String
    ) {
        val sharedPreferences = context?.getSharedPreferences("courseList", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("key1", value1)
        editor?.putString("key2", value2)
        editor?.apply()
    }

    //
    private fun loadFromFirebase(code: String, category: String) {
        val db = FirebaseDatabase.getInstance().reference
        val ref = db.child("Study")
            .child("2nd Year").child(code).child(category)

        val mItems = ArrayList<CourseChapterLessonList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    snapshot.children.forEach {

                        val data = it.getValue(CourseChapterLessonList::class.java)
                        mItems.add(data!!)
                        Log.i("pdfList", it.toString())

                        //
                        val intent = Intent(context, PdfViewerActivity::class.java)
                        intent.putExtra("lesson", data.chapter)
                        intent.putExtra("pdfLink", data.fileUrl)
                        startActivity(intent)

                    }
                } else {
                    Toast.makeText(context, "Not yet uploaded", Toast.LENGTH_SHORT).show()
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