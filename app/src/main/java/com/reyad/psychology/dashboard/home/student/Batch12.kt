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
import com.reyad.psychology.databinding.FragmentBatch12Binding

private const val TAG = "batch12"

class Batch12 : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var _binding: FragmentBatch12Binding
    private val binding get() = _binding

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatch12Binding.inflate(inflater, container, false)
        val view = binding.root

        //
        progressBar = binding.progressBarBatch12

        //
        retrieveBatch12()

        //
        binding.swipeRefreshBatch12.setOnRefreshListener {
            retrieveBatch12()
        }

        return view
    }

    private fun retrieveBatch12() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Students").child("Batch 12").orderByChild("priority")

        //firebase offline
        ref.keepSynced(true)

        val items = ArrayList<StudentItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val data = it.getValue(StudentItemList::class.java)
                        items.add(data!!)

                        recyclerView = binding.recyclerViewBatch12
                        recyclerView.layoutManager = LinearLayoutManager(context)

                        val adapter = StudentAdapter(requireContext(), items)
                        recyclerView.adapter = adapter

                        Log.i("batch12", data.name)
                        binding.swipeRefreshBatch12.isRefreshing = false
                        progressBar.visibility = View.GONE
                    }

                } else {
                    binding.tvNoDataBatch12.visibility = View.VISIBLE
                    binding.swipeRefreshBatch12.isRefreshing = false
                    progressBar.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                binding.swipeRefreshBatch12.isRefreshing = false
                progressBar.visibility = View.GONE
                Log.i(TAG, "$TAG error:${error.message} ")
                Toast.makeText(context, "$TAG error:${error.message} ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
