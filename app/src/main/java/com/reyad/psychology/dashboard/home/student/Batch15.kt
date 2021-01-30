package com.reyad.psychology.dashboard.home.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentBatch15Binding

private const val TAG = "batch15"

class Batch15 : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var _binding: FragmentBatch15Binding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatch15Binding.inflate(inflater, container, false)
        val view = binding.root


        retrieveBatch15()

        return view
    }

    private fun retrieveBatch15() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Students").child("Batch 15").orderByChild("priority")

        //firebase offline
        ref.keepSynced(true)

        val items = ArrayList<StudentItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val data = it.getValue(StudentItemList::class.java)
                        items.add(data!!)

                        recyclerView = binding.recyclerViewBatch15
                        recyclerView.layoutManager = LinearLayoutManager(context)

                        val adapter = StudentAdapter(requireContext(), items)
                        recyclerView.adapter = adapter

                        Log.i("batch15", data.name)
                    }

                }else{
                    binding.tvNoDataBatch15.visibility =View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "$TAG error:${error.message} ")
                Toast.makeText(context, "$TAG error:${error.message} ", Toast.LENGTH_SHORT).show()
            }
        })
    }

}