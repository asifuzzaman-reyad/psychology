package com.reyad.psychology.dashboard.study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R


class PdfDetailsAdapter(
    val context: Context,
    private val mList: ArrayList<PdfItemList>
) :
    RecyclerView.Adapter<PdfDetailsAdapter.MyViewHolder>() {

    // onCount
    override fun getItemCount(): Int = mList.size

    // onCreateView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.model_resource, parent, false)
        return MyViewHolder(view)
    }

    // onBind
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mListPosition = mList[position]
        holder.textViewLessonNo.text = mListPosition.lessonNo
        holder.textViewLessonTopic.text = mListPosition.lessonTopic

        val lesson = mListPosition.lessonNo
        val link = mListPosition.fileUrl

        holder.textViewRead.setOnClickListener {
            val intent = Intent(context, PdfViewerActivity::class.java)
            intent.putExtra("lesson", lesson)
            intent.putExtra("pdfLink", link)
            context.startActivity(intent)
        }

    }

    // view holder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewLessonNo: TextView = itemView.findViewById(R.id.tv_resource_title)
        val textViewLessonTopic: TextView = itemView.findViewById(R.id.tv_resource_lesson)
        val textViewRead: TextView = itemView.findViewById(R.id.tv_resource_read)
    }

}