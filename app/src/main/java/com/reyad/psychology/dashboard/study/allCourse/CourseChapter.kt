package com.reyad.psychology.dashboard.study.allCourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.ActivityCourseChapterBinding

class CourseChapter : AppCompatActivity() {

    private lateinit var binding: ActivityCourseChapterBinding

    private var getYear: String? = null
    private var getCourseCode: String? = null
    private var getCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getYear = intent.getStringExtra("year").toString()
        getCourseCode = intent.getStringExtra("courseCode").toString()
        getCategory = intent.getStringExtra("category").toString()

        val tvChapter = "Chapter List ($getCourseCode)"
        binding.tvHeaderCoursesChapter.text = tvChapter

        //
        binding.progressBarChapterLesson.visibility = View.VISIBLE

        //
        retrieveFirebaseData()
        binding.swipeRefreshChapter.setOnRefreshListener {
            retrieveFirebaseData()
        }

    }

    //
    private fun retrieveFirebaseData() {

        val db = FirebaseDatabase.getInstance().reference
        val ref = db.child("Study")
            .child(getYear!!)
            .child(getCourseCode!!)
            .child(getCategory!!)

        val mItems = ArrayList<CourseChapterList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    snapshot.children.forEach { snap ->

                        val courseChapterList = CourseChapterList()
                        courseChapterList.year = getYear!!
                        courseChapterList.courseCode = getCourseCode!!
                        courseChapterList.category = getCategory!!
                        courseChapterList.chapter = snap.key.toString()

                        Log.i("courseChapter", snap.key.toString())

                        mItems.add(courseChapterList)
                        val adapter = CourseChapterAdapter(this@CourseChapter, mItems)

                        binding.recyclerViewCourseChapter.adapter = adapter

                        binding.progressBarChapterLesson.visibility = View.GONE
                        binding.swipeRefreshChapter.isRefreshing = false
                    }
                } else {
                    binding.progressBarChapterLesson.visibility = View.GONE
                    binding.ivNoDataChapterLesson.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CourseChapter, "Resource Data Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBarChapterLesson.visibility = View.GONE
                binding.ivNoDataChapterLesson.visibility = View.VISIBLE
                binding.swipeRefreshChapter.isRefreshing = false

            }
        })

    }


}