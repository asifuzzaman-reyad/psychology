package com.reyad.psychology.messenger.users

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.databinding.ActivityUserProfileBinding
import com.reyad.psychology.messenger.UserItems
import java.text.DateFormat
import java.util.*

private const val TAG = "userProfile"

@SuppressLint("SetTextI18n")
class UserProfile : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var mProgressDialog: ProgressDialog

    private lateinit var mCurrentUser: FirebaseUser
    private lateinit var mUsersDatabase: DatabaseReference
    private lateinit var mFriendRequestDatabase: DatabaseReference
    private lateinit var mFriendListDatabase: DatabaseReference

    private lateinit var currentState: String
    private lateinit var getUserId: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //
        currentState = "not_friend"

        //
        progressDialog()

        // // get selected user
        getUserId = intent.getStringExtra("userId").toString()

        // init firebase
        mCurrentUser = FirebaseAuth.getInstance().currentUser!!
        mUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(getUserId)
        mFriendRequestDatabase = FirebaseDatabase.getInstance().reference.child("Friend_Request")
        mFriendListDatabase = FirebaseDatabase.getInstance().reference.child("Friend_List")

        // retrieve and set user data
        retrieveUserItems()

        // 3 -> --------------- SEND FRIEND REQUEST FEATURE ------------
        ifSendFriendRequest()

        // button send friend request
        binding.btnSendUserProfile.setOnClickListener {

            binding.btnSendUserProfile.isEnabled = false

            // 1 -> --------------- NOT FRIENDS STATE ------------
            ifNotFriend()

            // 2 -> --------------- CANCEL FRIEND REQUEST STATE ------------
            ifCancelFriendRequest()

            // 4 -> --------------- ACCEPT/DECLINE FRIEND REQUEST + FRIEND LIST FEATURE ------------
            ifAcceptorDeclineFriendRequest()

        }

        // button send message
        binding.btnSentMessageUserProfile.setOnClickListener {
            sendToMessageChatActivity()
        }

    }


    // retrieve and set user data
    private fun retrieveUserItems() {

        mUsersDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val userItems = snapshot.getValue(UserItems::class.java)

                // get put extra
                val getName = userItems?.name.toString()
                val getId = userItems?.id.toString()
                val getBatch = userItems?.batch.toString()

                // set text
                binding.tvNameUserProfile.text = getName
                binding.tvIdUserProfile.text = "Id: $getId"
                binding.tvBatchUserProfile.text = getBatch
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // 1 -> if user Not Friend
    private fun ifNotFriend() {

        if (currentState == "not_friend") {

            mFriendRequestDatabase.child(mCurrentUser.uid).child(getUserId).child("requestType")
                .setValue("sent")
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        currentState = "req_sent"
                        binding.btnSendUserProfile.text = "Cancel Friend Request"

                        mFriendRequestDatabase.child(getUserId).child(mCurrentUser.uid)
                            .child("requestType")
                            .setValue("received")
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Request send successfully ",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                    } else {
                        Toast.makeText(this, "Failed sending request", Toast.LENGTH_SHORT)
                            .show()
                    }

                    binding.btnSendUserProfile.isEnabled = true

                }
        }

    }

    // 2 -> if cancel Friend request
    private fun ifCancelFriendRequest() {

        if (currentState == "req_sent") {

            mFriendRequestDatabase.child(mCurrentUser.uid).child(getUserId)
                .removeValue()
                .addOnCompleteListener {
                    mFriendRequestDatabase.child(getUserId).child(mCurrentUser.uid)
                        .removeValue()
                        .addOnSuccessListener {

                            binding.btnSendUserProfile.isEnabled = true
                            currentState = "not_friend"
                            binding.btnSendUserProfile.text = "Send Friend Request"

                        }
                }

        }
    }

    // 3 -> if send Friend request to user
    private fun ifSendFriendRequest() {

        mFriendRequestDatabase.child(mCurrentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(getUserId)) {

                        val requestType = snapshot.child(getUserId)
                            .child("requestType").value.toString()

                        if (requestType == "received") {

                            currentState = "req_received"
                            binding.btnSendUserProfile.text = "Accept Friend Request"

                            binding.btnDeclineUserProfile.visibility = View.VISIBLE
                            binding.btnDeclineUserProfile.isEnabled = true

                        } else if (requestType == "sent") {

                            currentState = "req_sent"
                            binding.btnSendUserProfile.text = "Cancel Friend Request"
                        }

                        mProgressDialog.dismiss()

                    } else {

                        mFriendListDatabase.child(mCurrentUser.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    if (snapshot.hasChild(getUserId)) {
                                        currentState = "friends"
                                        binding.btnSendUserProfile.text = " UnFriend This Person "

                                        binding.btnDeclineUserProfile.visibility = View.INVISIBLE

                                    }

                                    mProgressDialog.dismiss()

                                }

                                override fun onCancelled(error: DatabaseError) {

                                    mProgressDialog.dismiss()

                                }

                            })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    // 4 -> if accept/decline friend request
    private fun ifAcceptorDeclineFriendRequest() {

        val currentDate = DateFormat.getDateInstance().format(Date())

        if (currentState == "req_received") {
            mFriendListDatabase.child(mCurrentUser.uid).child(getUserId).child("date")
                .setValue(currentDate)
                .addOnSuccessListener {

                    mFriendListDatabase.child(getUserId).child(mCurrentUser.uid).child("date")
                        .setValue(currentDate)
                        .addOnSuccessListener {

                            mFriendRequestDatabase.child(mCurrentUser.uid).child(getUserId)
                                .removeValue()
                                .addOnCompleteListener {

                                    mFriendRequestDatabase.child(getUserId).child(mCurrentUser.uid)
                                        .removeValue()
                                        .addOnSuccessListener {

                                            binding.btnSendUserProfile.isEnabled = true
                                            currentState = "friends"
                                            binding.btnSendUserProfile.text =
                                                " UnFriend This Person "

                                            binding.btnDeclineUserProfile.visibility =
                                                View.INVISIBLE

                                        }

                                }

                        }
                }
        }
    }


    // send To MessageChatActivity
    private fun sendToMessageChatActivity() {
        val messageChatIntent = Intent(this, MessageChatActivity::class.java).apply {
            putExtra("userId", getUserId)
        }
        startActivity(messageChatIntent)
        finish()
    }

    //progress
    private fun progressDialog() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Loading User Data")
        mProgressDialog.setMessage("Please wait while we load the user data.")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

}