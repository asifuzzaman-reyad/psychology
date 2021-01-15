package com.reyad.psychology.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.dashboard.home.student.StudentItemList

class HomeRepository(val app: Application) {

    val repoData13 = MutableLiveData<List<StudentItemList>>()
    val repoData14 = MutableLiveData<List<StudentItemList>>()

    init {
        callFirebaseService("Batch 13", repoData13)
        callFirebaseService("Batch 14", repoData14)
    }

    //
    private fun callFirebaseService(batch: String, mutableStudentList: MutableLiveData<List<StudentItemList>>) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Students").child(batch).orderByChild("priority")

        val items = ArrayList<StudentItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data = it.getValue(StudentItemList::class.java)
                    items.add(data!!)

                    mutableStudentList.value = items
                    Log.i("teach", data.name)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("homeRepo", error.message)
            }
        })
    }

}
