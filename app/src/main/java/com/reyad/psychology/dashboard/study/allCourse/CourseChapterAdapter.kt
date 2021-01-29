package com.reyad.psychology.dashboard.study.allCourse

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R


class CourseChapterAdapter(
    val context: Context,
    private val mList: ArrayList<CourseChapterList>
) :
    RecyclerView.Adapter<CourseChapterAdapter.MyViewHolder>() {

    // onCount
    override fun getItemCount(): Int = mList.size

    // onCreateView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.model_chapter, parent, false)
        return MyViewHolder(view)
    }

    // onBind
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mListPosition = mList[position]

        holder.textViewChapter.text = mListPosition.chapter

        holder.linearChapter.setOnClickListener {
            val intent = Intent(context, CourseChapterLesson::class.java).apply {
                putExtra("year", mListPosition.year)
                putExtra("courseCode", mListPosition.courseCode)
                putExtra("category", "Notes")
                putExtra("chapter", mListPosition.chapter)
            }
            context.startActivity(intent)
        }

    }

    // view holder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewChapter: TextView = itemView.findViewById(R.id.tv_resource_chapter)
        val linearChapter: LinearLayout = itemView.findViewById(R.id.linear_course_chapter)
    }

}