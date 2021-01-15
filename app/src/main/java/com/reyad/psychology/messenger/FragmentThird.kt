package com.reyad.psychology.messenger

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentThirdBinding

private const val TAG = "fragmentThird"

class FragmentThird : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        val view = binding.root

        // friend list
        retrieveFriendList()

        return view
    }

    //  retrieveFriendList
    private fun retrieveFriendList() {
        val friendListDatabase = FirebaseDatabase.getInstance().reference.child("Friend_List")

        class FriendList(val date: String) {
            constructor() : this("")
        }

        val mCurrentUser = FirebaseAuth.getInstance().uid
        Log.i(TAG, "currentUser: $mCurrentUser")

        val friendList: ArrayList<FriendList> = ArrayList()

        friendListDatabase.child(mCurrentUser.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val data = snapshot.getValue(FriendList::class.java)
                        friendList.add(data!!)

                        Log.i(TAG, "name: ${data.date}")

//                val adapter = FriendListAdapter(this)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    // on destroy
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

//class FriendListAdapter(): FirebaseRecyclerAdapter<RecyclerView.ViewHolder> {

//}
