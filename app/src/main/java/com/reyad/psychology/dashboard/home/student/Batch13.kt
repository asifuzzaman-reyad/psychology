package com.reyad.psychology.dashboard.home.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentBatch13Binding

private const val TAG = "batch13"

class Batch13 : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var _binding: FragmentBatch13Binding
    private val binding get() = _binding

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBatch13Binding.inflate(inflater, container, false)
        val view = binding.root

        //
        progressBar = binding.progressBarBatch13

        //
        retrieveBatch13()

        //
        binding.swipeRefreshBatch13.setOnRefreshListener {
            retrieveBatch13()
        }

        return view
    }

    private fun retrieveBatch13() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Students").child("Batch 13").orderByChild("priority")

        //firebase offline
        ref.keepSynced(true)

        val items = ArrayList<StudentItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data = it.getValue(StudentItemList::class.java)
                    items.add(data!!)


                    recyclerView = binding.recyclerViewBatch13
                    recyclerView.layoutManager = LinearLayoutManager(context)

                    val adapter = StudentAdapter(requireContext(), items)
                    recyclerView.adapter = adapter

                    Log.i("batch13", data.name)
                    binding.swipeRefreshBatch13.isRefreshing = false
                    progressBar.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.swipeRefreshBatch13.isRefreshing = false
                progressBar.visibility = View.GONE
                Log.i(TAG, "$TAG error:${error.message} ")
                Toast.makeText(context, "$TAG error:${error.message} ", Toast.LENGTH_SHORT).show()
            }
        })
    }


}