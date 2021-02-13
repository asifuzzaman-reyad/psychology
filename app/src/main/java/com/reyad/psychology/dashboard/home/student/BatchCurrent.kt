package com.reyad.psychology.dashboard.home.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reyad.psychology.databinding.ActivityBatchCurrentBinding
import dmax.dialog.SpotsDialog

private const val TAG = "batchCurrent"

class BatchCurrent : AppCompatActivity() {

    private lateinit var binding: ActivityBatchCurrentBinding

    private lateinit var dialog: SpotsDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBatchCurrentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentBatch = intent.getStringExtra("batch").toString()

        //
        dialog = SpotsDialog.Builder().setContext(this)
            .build() as SpotsDialog
        dialog.show()

        //set current batch
        binding.tvAllBatchCurrent.text = currentBatch

        //go to student main
        binding.btnAllBatchCurrent.setOnClickListener {
            val intentStudentMain = Intent(this, StudentMain::class.java)
            startActivity(intentStudentMain)
        }

        //
        retrieveBatchCurrent(currentBatch)

        //
        binding.swipeRefreshBatchCurrent.setOnRefreshListener {
            retrieveBatchCurrent(currentBatch)
        }
    }

    //
    private fun retrieveBatchCurrent(currentBatch: String) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Students").child(currentBatch).orderByChild("priority")

        //firebase offline
        ref.keepSynced(true)

        val items = ArrayList<StudentItemList>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val data = it.getValue(StudentItemList::class.java)
                        items.add(data!!)

                        val recyclerView = binding.recyclerViewBatchCurrent

                        val adapter = StudentAdapterCurrent(this@BatchCurrent, items)
                        recyclerView.adapter = adapter

                        Log.i("batch15", data.name)
                        binding.swipeRefreshBatchCurrent.isRefreshing = false
                        dialog.dismiss()
                    }

                } else {
                    binding.tvNoDataBatchCurrent.visibility = View.VISIBLE
                    binding.swipeRefreshBatchCurrent.isRefreshing = false
                    dialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.swipeRefreshBatchCurrent.isRefreshing = false
                dialog.dismiss()
                Log.i(TAG, "$TAG error:${error.message} ")
                Toast.makeText(
                    this@BatchCurrent,
                    "$TAG error:${error.message} ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}