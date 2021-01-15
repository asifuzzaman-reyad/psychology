package com.reyad.psychology.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.reyad.psychology.R
import com.reyad.psychology.databinding.FragmentFirstBinding
import com.reyad.psychology.messenger.users.ChatItems
import com.reyad.psychology.messenger.users.MessageChatActivity
import com.reyad.psychology.messenger.users.UserProfile
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

private const val TAG = "latestChat"

class FragmentFirst : Fragment() {
    private lateinit var _binding: FragmentFirstBinding
    private val binding get() = _binding

    private var getUserId: String? = null
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerViewLatestChat.setHasFixedSize(true)
        binding.recyclerViewLatestChat.adapter = adapter

        //
        retrieveLatestMessage()

        return view
    }

    //
    val latestMessageHashMap = HashMap<String, ChatItems>()
    private fun refreshRecyclerViewMessage() {
        adapter.clear()
        latestMessageHashMap.values.forEach {
            adapter.add(LatestMessageRow(requireContext(), it))
        }
    }

    // retrieve Message
    private fun retrieveLatestMessage() {

        val fromId = FirebaseAuth.getInstance().currentUser!!.uid

        val chatDatabaseRef =
            FirebaseDatabase.getInstance().getReference("/latest_messages/$fromId")

        chatDatabaseRef.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val latestChatItems = snapshot.getValue(ChatItems::class.java) ?: return
                latestMessageHashMap[snapshot.key!!] = latestChatItems
                refreshRecyclerViewMessage()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val latestChatItems = snapshot.getValue(ChatItems::class.java) ?: return
                    latestMessageHashMap[snapshot.key!!] = latestChatItems
                    refreshRecyclerViewMessage()
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


    // chat to id
    class LatestMessageRow(val context: Context, private val chatItems: ChatItems?) :
        Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.tv_msg_latest_chat_model)
                .text = chatItems!!.message

            val chatPartner: String
            if (chatItems.fromId == FirebaseAuth.getInstance().uid) {
                chatPartner = chatItems.toId
            } else {
                chatPartner = chatItems.fromId
            }

            // get user
            val ref = FirebaseDatabase.getInstance().getReference("/Users/$chatPartner")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(UserItems::class.java)
                        viewHolder.itemView.findViewById<TextView>(R.id.tv_name_latest_chat_model)
                            .text = user!!.name
                        val imageUrl =
                            "https://scontent.fjsr1-1.fna.fbcdn.net/v/t1.0-9/70013353_952371095103880_5248495691214356480_o.jpg?_nc_cat=108&_nc_sid=09cbfe&_nc_eui2=AeHOtMllFZmWUDTwPN8dxWYXtcsM-vhO4Fu1ywz6-E7gW4uXXKqFGRrVwGQ8yMoBN7TZ8QU3aAkgoKtJNdS0v9Ob&_nc_ohc=OqbZeGAZ_IUAX-0GAcm&_nc_ht=scontent.fjsr1-1.fna&oh=238348477852f3e4417009b3d04d652a&oe=5F12B152"
                        Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
                            .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.civ_profile_latest_chat_model))

                        //
                        viewHolder.itemView.findViewById<ConstraintLayout>(R.id.constraint_latest_chat)
                            .setOnClickListener {
                                val chatIntent =
                                    Intent(context, MessageChatActivity::class.java).apply {
                                        putExtra("userId", chatPartner)
                                    }
                                context.startActivity(chatIntent)
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        override fun getLayout(): Int {
            return R.layout.model_latest_chat
        }

    }

}