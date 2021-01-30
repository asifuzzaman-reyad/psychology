package com.reyad.psychology.dashboard.home.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.FragmentTeacherAbsentBinding

private const val TAG = "teacherAbsent"

class TeacherAbsent : Fragment(), TeacherAdapter.TeacherItemListener {

    private lateinit var _binding: FragmentTeacherAbsentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherAbsentBinding.inflate(inflater, container, false)
        val view = binding.root

        retrieveTeacher()
        return view
    }

    //
    private fun retrieveTeacher() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Teacher").child("Absent")

        //firebase offline
        ref.keepSynced(true)

        val items = ArrayList<TeacherItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data = it.getValue(TeacherItemList::class.java)
                    items.add(data!!)

                    val adapter = TeacherAdapter(requireContext(), items, this@TeacherAbsent)
                    binding.recyclerViewAbsent.adapter = adapter
                    Log.i(TAG, data.name)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "$TAG error:${error.message} ")
                Toast.makeText(context, "$TAG error:${error.message} ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //
    override fun onItemClick(item: TeacherItemList) {
        val intent = Intent(requireContext(), TeacherDetailsActivity::class.java).apply {
            putExtra("name", item.name)
            putExtra("phd", item.phd)
            putExtra("post", item.post)
            putExtra("interest", item.interest)
            putExtra("publication", item.publication)
            putExtra("imageUrl", item.imageUrl)
            putExtra("mobile", item.mobile)
            putExtra("email", item.email)
        }
        requireContext().startActivity(intent)

    }

}
