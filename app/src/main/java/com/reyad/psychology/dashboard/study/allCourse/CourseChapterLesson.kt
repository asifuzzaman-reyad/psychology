package com.reyad.psychology.dashboard.study.allCourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import com.reyad.psychology.databinding.ActivityCourseChapterLessonBinding

class CourseChapterLesson : AppCompatActivity() {

    private lateinit var binding: ActivityCourseChapterLessonBinding

    private var getYear: String? = null
    private var getCourseCode: String? = null
    private var getCategory: String? = null
    private var getChapter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseChapterLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        getYear = intent.getStringExtra("year").toString()
        getCourseCode = intent.getStringExtra("courseCode").toString()
        getCategory = intent?.getStringExtra("category").toString()
        getChapter = intent?.getStringExtra("chapter").toString()

        val tvLesson = "$getCategory Details ($getCourseCode)"
        binding.tvHeaderChapterDetails.text = tvLesson
        Log.i(
            "courseDetails", " ${getCourseCode.toString()} ->> " +
                    "${getCategory.toString()} ->> ${getChapter.toString()}"
        )

        //
        binding.progressBarChapterDetails.visibility = View.VISIBLE

        //
        retrieveFirebaseData()

        binding.swipeRefreshChapterLesson.setOnRefreshListener {
            retrieveFirebaseData()
        }

    }

    //
    private fun retrieveFirebaseData() {
        val db = FirebaseDatabase.getInstance().reference
        val courseLessonListMain = ArrayList<CourseChapterLessonList>()

        if (getCategory != "Notes") {
            val ref = db.child("Study")
                .child(getYear!!)
                .child(getCourseCode!!)
                .child(getCategory!!)

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap ->

                            val data = snap.getValue(CourseChapterLessonList::class.java)
                            courseLessonListMain.add(data!!)
                            Log.i("courseChapterDetails", snap.toString())

                            val adapter =
                                PdfDetailsAdapter(this@CourseChapterLesson, courseLessonListMain)

                            binding.recyclerViewChapterDetails.adapter = adapter
                            binding.progressBarChapterDetails.visibility = View.GONE
                            binding.swipeRefreshChapterLesson.isRefreshing = false

                        }

                    } else {
                        binding.progressBarChapterDetails.visibility = View.GONE
                        binding.ivNoDataChapterDetails.visibility = View.VISIBLE
                        binding.swipeRefreshChapterLesson.isRefreshing = false
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Resource Data Error: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarChapterDetails.visibility = View.GONE
                    binding.ivNoDataChapterDetails.visibility = View.VISIBLE
                    binding.swipeRefreshChapterLesson.isRefreshing = false
                }
            })

        } else {

            val ref1 = db.child("Study")
                .child(getYear!!)
                .child(getCourseCode!!)
                .child(getCategory!!)
                .child(getChapter!!)
                .orderByChild("serialNo")

            ref1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap ->

                            val data = snap.getValue(CourseChapterLessonList::class.java)
                            courseLessonListMain.add(data!!)
                            Log.i("courseChapterDetails", snap.toString())

                            val adapter =
                                PdfDetailsAdapter(this@CourseChapterLesson, courseLessonListMain)

                            binding.recyclerViewChapterDetails.adapter = adapter
                            binding.progressBarChapterDetails.visibility = View.GONE
                            binding.swipeRefreshChapterLesson.isRefreshing = false

                        }

                    } else {
                        binding.progressBarChapterDetails.visibility = View.GONE
                        binding.ivNoDataChapterDetails.visibility = View.VISIBLE
                        binding.swipeRefreshChapterLesson.isRefreshing = false
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Resource Data Error: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarChapterDetails.visibility = View.GONE
                    binding.ivNoDataChapterDetails.visibility = View.VISIBLE
                    binding.swipeRefreshChapterLesson.isRefreshing = false
                }
            })

        }

    }
}