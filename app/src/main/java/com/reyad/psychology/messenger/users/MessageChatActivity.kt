package com.reyad.psychology.messenger.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityMessageChatBinding
import com.reyad.psychology.notification.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "chatActivity"

class MessageChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageChatBinding

    val adapter = GroupAdapter<GroupieViewHolder>()

    var notify = false
    var apiService: APIService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // recycler adapter
        binding.recyclerViewChatMessage.setHasFixedSize(true)
        binding.recyclerViewChatMessage.adapter = adapter

        // retrieve message
        retrieveMessage()

//        send message
        binding.btnMessageSend.setOnClickListener {
            sendMessage()
        }

    }

    // chat from id
    class ChatFromId(val text: String) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.tv_message_right).text = text
        }

        override fun getLayout(): Int {
            return R.layout.model_message_right
        }

    }

    // chat to id
    class ChatToId(val text: String) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.tv_message_left).text = text
        }

        override fun getLayout(): Int {
            return R.layout.model_message_left
        }

    }

    // send message
    private fun sendMessage() {
        val message = binding.etMessageChatActivity.text.toString()

        if (message == "") {
            Toast.makeText(this, "write a message first", Toast.LENGTH_SHORT).show()

        } else {
            val userId = intent.getStringExtra("userId")
            val toId = userId.toString()
            val fromId = FirebaseAuth.getInstance().uid
            val timeStamp = System.currentTimeMillis() / 1000

            val fromReference =
                FirebaseDatabase.getInstance().getReference("/user_messages/$fromId/$toId").push()

            val toReference =
                FirebaseDatabase.getInstance().getReference("/user_messages/$toId/$fromId").push()

            val chatItems =
                ChatItems(fromId!!, toId, message, fromReference.key!!, timeStamp, false)

            fromReference.setValue(chatItems)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "sent", Toast.LENGTH_SHORT).show()

                        // clear edit text
                        binding.etMessageChatActivity.text.clear()

                        // scroll up last message
                        binding.recyclerViewChatMessage.scrollToPosition(adapter.itemCount - 1)

                        // send notifications
                        if (notify) {
                            sendNotification(fromId, toId, "reyad", "hi")
                        }

                    } else {
                        notify = false
                    }
                }

            //
            toReference.setValue(chatItems)

            // last message
            val latestMessageFromRef =
                FirebaseDatabase.getInstance().getReference("/latest_messages/$fromId/$toId")
            latestMessageFromRef.setValue(chatItems)

            // last message
            val latestMessageToRef =
                FirebaseDatabase.getInstance().getReference("/latest_messages/$toId/$fromId")
            latestMessageToRef.setValue(chatItems)

        }
    }

    // retrieve Message
    private fun retrieveMessage() {
        val userId = intent.getStringExtra("userId")
        val toId = userId.toString()
        val fromId = FirebaseAuth.getInstance().uid


        val chatDatabaseRef =
            FirebaseDatabase.getInstance().getReference("/user_messages/$fromId/$toId")

        chatDatabaseRef.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItems = snapshot.getValue(ChatItems::class.java)
                Log.i(TAG, "chat: ${chatItems?.message}")

                if (chatItems!!.fromId == fromId) {
                    adapter.add(ChatFromId(chatItems.message))
                } else {
                    adapter.add(ChatToId(chatItems.message))
                }

                // scroll up last message
                binding.recyclerViewChatMessage.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


    // push notification
    private fun sendNotification(
        receiveId: String,
        senderId: String,
        userName: String?,
        message: String
    ) {
        val ref = FirebaseDatabase.getInstance().reference.child("tokens")

        val query = ref.orderByKey().equalTo(receiveId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val token: Token? = snap.getValue(Token::class.java)
                    val data = Data(
                        FirebaseAuth.getInstance().uid.toString(),
                        com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark,
                        message,
                        "$userName",
                        senderId
                    )
                    val sender = Sender(data, token!!.getToken().toString())
                    apiService!!.sendNotification(sender)
                        .enqueue(object : Callback<MyResponse> {
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if (response.code() == 200) {
                                    if (response.body()!!.success != 1) {
                                        Toast.makeText(
                                            this@MessageChatActivity,
                                            " Notification Failed",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}