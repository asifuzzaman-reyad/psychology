package com.reyad.psychology.dashboard.home.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentBatch14Binding

class Batch14 : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var _binding: FragmentBatch14Binding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatch14Binding.inflate(inflater, container, false)
        val view = binding.root

        retrieveBatch14()

        return view
    }

    private fun retrieveBatch14() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Students").child("Batch 14").orderByChild("priority")

        val items = ArrayList<StudentItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data = it.getValue(StudentItemList::class.java)
                    items.add(data!!)


                    recyclerView = binding.recyclerViewBatch14
                    recyclerView.layoutManager = LinearLayoutManager(context)

                    val adapter = StudentAdapter(requireContext(), items)
                    recyclerView.adapter = adapter

                    Log.i("batch14", data.name)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}