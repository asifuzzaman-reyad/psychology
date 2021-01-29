package com.reyad.psychology.dashboard.study.allCourse

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.reyad.psychology.R


class PdfDetailsAdapter(
    val context: Context,
    private val mList: ArrayList<CourseChapterLessonList>
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
        val lessonNo = mListPosition.lessonName
        val link = mListPosition.fileUrl

        holder.textViewLessonName.text = lessonNo
        holder.textViewLessonSource.text = mListPosition.source


        holder.buttonView.setOnClickListener {
            val intent = Intent(context, PdfViewerActivity::class.java)
            intent.putExtra("lesson", lessonNo)
            intent.putExtra("pdfLink", link)
            context.startActivity(intent)
        }

        holder.buttonDownload.setOnClickListener {
            Toast.makeText(context, "Start Downloading...", Toast.LENGTH_SHORT).show()
            val request = DownloadManager.Request(Uri.parse(link))
            request.setTitle(lessonNo)
            request.setDescription("Downloading $lessonNo")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.allowScanningByMediaScanner()
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, lessonNo)
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }

    }

    // view holder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewLessonName: TextView = itemView.findViewById(R.id.tv_resource_lesson_name)
        val textViewLessonSource: TextView = itemView.findViewById(R.id.tv_resource_lesson_source)
        val buttonView: MaterialButton = itemView.findViewById(R.id.btn_resource_view)
        val buttonDownload: TextView = itemView.findViewById(R.id.btn_resource_download)
    }

}