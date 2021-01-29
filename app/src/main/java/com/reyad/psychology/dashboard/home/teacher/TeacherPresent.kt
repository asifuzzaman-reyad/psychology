package com.reyad.psychology.dashboard.home.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentTeacherPresentBinding

class TeacherPresent : Fragment(), TeacherAdapter.TeacherItemListener {

    private lateinit var _binding: FragmentTeacherPresentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTeacherPresentBinding.inflate(inflater, container, false)
        val view = binding.root

        val teacherItemList: List<TeacherItemList> = ArrayList()

        val adapter = TeacherAdapter(requireContext(), teacherItemList, this)
        binding.recyclerViewPresent.adapter = adapter

        //
        retrieveTeacher()

        return view
    }

    //
    private fun retrieveTeacher() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Teacher").child("Present")

        val items = ArrayList<TeacherItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data = it.getValue(TeacherItemList::class.java)
                    items.add(data!!)

                    val adapter = TeacherAdapter(requireContext(), items, this@TeacherPresent)
                    binding.recyclerViewPresent.adapter = adapter
                    Log.i("teacher", data.name)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //
    override fun onItemClick(item: TeacherItemList) {

        val intent = Intent(requireContext(), TeacherDetailsActivity::class.java).apply {
            putExtra("name", item.name)
            putExtra("post", item.post)
            putExtra("phd", item.phd)
            putExtra("interest", item.interest)
            putExtra("publication", item.publication)
            putExtra("imageUrl", item.imageUrl)
            putExtra("mobile", item.mobile)
            putExtra("email", item.email)
        }
        requireContext().startActivity(intent)

    }
}