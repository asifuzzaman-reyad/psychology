package com.reyad.psychology.dashboard.home.teacher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R
import com.squareup.picasso.Picasso


class TeacherAdapter(
    val context: Context,
    val data: List<TeacherItemList>,
    private val teacherItemListener: TeacherItemListener
) :
    RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    //    studentItem ->> item count
    override fun getItemCount(): Int = data.size

    //inflate ->> model layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.model_teacher_head, parent, false)
        return TeacherViewHolder(view)
    }

    //holder model
    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val itemPos: TeacherItemList = data[position]  //get position from array

        holder.textNameT.text = itemPos.name
        holder.textPost.text = itemPos.post
        Picasso.get().load(itemPos.imageUrl).into(holder.imageUrlT)

        holder.itemView.setOnClickListener {
            teacherItemListener.onItemClick(itemPos)
        }
    }

    // student view holder
    class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textNameT: TextView = itemView.findViewById(R.id.textViewName_teacher_head)
        var textPost: TextView = itemView.findViewById(R.id.textView_post_teacher_head)
        var imageUrlT: ImageView = itemView.findViewById(R.id.imageView_teacher_model)
    }

    interface TeacherItemListener {
        fun onItemClick(item: TeacherItemList)
    }
}

