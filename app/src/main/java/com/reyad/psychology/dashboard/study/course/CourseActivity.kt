package com.reyad.psychology.dashboard.study.course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.reyad.psychology.databinding.ActivityCourseBinding
import dmax.dialog.SpotsDialog

class CourseActivity : AppCompatActivity() {

    lateinit var binding: ActivityCourseBinding
    lateinit var dialog: android.app.AlertDialog
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = SpotsDialog.Builder().setContext(this).build()
        val courseCode = intent.getStringExtra("courseCode").toString()
        binding.tvCourseCode.text = courseCode

        mDatabase = FirebaseDatabase.getInstance().getReference("Course").child(courseCode)

        getFirebaseData()
    }

    //
    private fun getFirebaseData() {
        dialog.show()
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemGroupA = ArrayList<ItemGroup>()

                if (snapshot.exists()) {
                    for (myDataSnapShot in snapshot.children) {
                        val itemGroup = ItemGroup()
                        itemGroup.headerTitle =
                            myDataSnapShot.child("headerTitle").getValue(true).toString()
                        Log.i("study2", myDataSnapShot.child("headerTitle").value.toString())
                        val t = object : GenericTypeIndicator<ArrayList<ItemData>>() {}
                        itemGroup.itemList = myDataSnapShot.child("listItem").getValue(t)
                        itemGroupA.add(itemGroup)

                        val adapter = MyGroupAdapter(this@CourseActivity, itemGroupA)
                        binding.recyclerViewStudy.adapter = adapter
                        dialog.dismiss()
                    }
                } else {
                    dialog.dismiss()
                    Toast.makeText(this@CourseActivity, "Not yet uploaded...", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("study2", error.message)
                dialog.dismiss()
            }
        })
    }
}