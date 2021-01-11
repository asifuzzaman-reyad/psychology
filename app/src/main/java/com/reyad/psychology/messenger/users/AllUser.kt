package com.reyad.psychology.messenger.users

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.reyad.psychology.databinding.ActivityAllUserBinding
import com.reyad.psychology.messenger.UserItems

private const val TAG = "allUser"

class AllUser : AppCompatActivity() {
    private lateinit var binding: ActivityAllUserBinding

    private lateinit var mCurrentUser: FirebaseUser
    private lateinit var mUserDatabaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // firebase
        mCurrentUser = FirebaseAuth.getInstance().currentUser!!
        mUserDatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")


        // all users data
        retrieveAllUser()

        //
        binding.etSearchAllUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i(TAG, "beforeTextChanged: ")
            }

            @SuppressLint("DefaultLocale")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUser(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged: ")
            }

        })

    }

    private fun retrieveAllUser() {

        mCurrentUser = FirebaseAuth.getInstance().currentUser!!
        mUserDatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        mUserDatabaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val userData : ArrayList<UserItems> = ArrayList()
                userData.clear()

                if (binding.etSearchAllUser.text.toString() == ""){

                    for (snap in snapshot.children){

                        val userItems = snap.getValue(UserItems::class.java)
                        if (mCurrentUser.uid != userItems!!.userId){

                            userData.add(userItems)

                            binding.recyclerViewAllUser.setHasFixedSize(true)
                            binding.recyclerViewAllUser.adapter = AllUserAdapter(this@AllUser, userData)
                        }

                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


    //
    private fun searchForUser(str: String){

        mCurrentUser = FirebaseAuth.getInstance().currentUser!!
        val queryUsers = FirebaseDatabase.getInstance().reference
            .child("Users").orderByChild("name")
            .startAt(str)
            .endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val userData : ArrayList<UserItems> = ArrayList()
                userData.clear()

                for (snap in snapshot.children){

                    val userItems = snap.getValue(UserItems::class.java)

                    if (mCurrentUser.uid != userItems!!.userId){

                        userData.add(userItems)

                        binding.recyclerViewAllUser.setHasFixedSize(true)
                        binding.recyclerViewAllUser.adapter = AllUserAdapter(this@AllUser, userData)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





    }

}
