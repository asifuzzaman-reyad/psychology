package com.reyad.psychology.dashboard.study

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import com.reyad.psychology.databinding.ActivityPdfViewerBinding
import java.io.File


class PdfViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfViewerBinding

    private var imageName: String? = null
    private var link: String? = null

    private var i: Int = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val lessonNo: String? = intent.getStringExtra("lesson")
        binding.tvTitlePdfViewer.text = lessonNo

        link = intent.getStringExtra("pdfLink")
        Log.i("link", link!!)

        haveStoragePermission()

        loadPdf(link)
        loadProgressBar()
        binding.btnDownloadPdfViewer.setOnClickListener {
            imageName = "$lessonNo.pdf"
            downloadImage(link, imageName)
        }
    }

    //
    private fun loadPdf(url: String?) {
        FileLoader.with(applicationContext)
            .load(url, false)
            .fromDirectory("pdfFile", FileLoader.DIR_INTERNAL)
            .asFile(object : FileRequestListener<File> {
                override fun onLoad(
                    request: FileLoadRequest?,
                    response: FileResponse<File>?
                ) {
                    binding.progressBarPdfViewer.visibility = View.GONE    //hide progress bar
                    binding.pdfProgressTv.visibility = View.GONE

                    val pdfFile: File = response!!.body

                    //load pdf from internet
                    binding.gitPdfView.fromFile(pdfFile)
                        .defaultPage(0)
                        .enableDoubletap(true)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .onDraw { canvas, pageWidth, pageHeight, displayedPage -> }
                        .onPageChange { page, pageCount -> }
                        .onPageError { page, t ->
                            Toast.makeText(
                                applicationContext, "Error while opening page",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            Log.d("Error", "" + t.localizedMessage)
                        }
                        .onTap { false }
                        .onRender { nbPages, pageWidth, pageHeight ->
                            //fit to screen size
                            binding.gitPdfView.fitToWidth()
                        }
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.RED)
                        .load()
                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    Toast.makeText(applicationContext, "" + t?.message, Toast.LENGTH_SHORT).show()

                    binding.progressBarPdfViewer.visibility = View.GONE   //hide progress bar
                    binding.pdfProgressTv.visibility = View.GONE
                }
            })
    }

    // download pdf
    private fun downloadImage(url: String?, outputFileName: String?) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(imageName)
        request.setDescription("Downloading $imageName")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.allowScanningByMediaScanner()
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outputFileName)
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    //
    @SuppressLint("SetTextI18n")
    fun loadProgressBar() {
        binding.progressBarPdfViewer.visibility = View.VISIBLE
        binding.pdfProgressTv.visibility = View.VISIBLE

        i = binding.progressBarPdfViewer.progress

        Thread {
            while (i < 100) {
                i += 4
                // Update the progress bar and display the current value
                handler.post {
                    binding.progressBarPdfViewer.progress = i
                    binding.pdfProgressTv.text =
                        i.toString() + "/" + binding.progressBarPdfViewer.max
                }
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    // runtime permission
    private fun haveStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("Permission error", "You have permission")
                true
            } else {
                Log.e("Permission error", "You have asked for permission")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission")
            true
        }
    }
}