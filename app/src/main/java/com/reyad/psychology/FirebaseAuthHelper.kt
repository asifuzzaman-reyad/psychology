package com.reyad.psychology

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseAuthHelper {

    companion object{

        fun retrieveFirebaseUsers() {
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

                        val batch = userData?.batch.toString()
                        val id = userData?.id.toString()

                        Log.i("companion", "$batch ---> $id")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}