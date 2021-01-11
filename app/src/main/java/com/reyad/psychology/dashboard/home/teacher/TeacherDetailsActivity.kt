package com.reyad.psychology.dashboard.home.teacher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityTeacherDetailsBinding
import com.squareup.picasso.Picasso

class TeacherDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //
        val name = intent.getStringExtra("name")
        val post = intent.getStringExtra("post")
        val phd = intent.getStringExtra("phd")
        val interest = intent.getStringExtra("interest")
        val publication = intent.getStringExtra("publication")
        val imageUrl = intent.getStringExtra("imageUrl")
        val mobile = intent.getStringExtra("mobile")
        val email = intent.getStringExtra("email")

        Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
            .into(binding.civProfileTeacherDetails)

        //
        binding.tvNameTeacherDetails.text = name
        binding.tvPostTeacherDetails.text = post
        binding.tvPhdTeacherDetails.text = phd
        binding.tvInterestTeacherDetails.text = interest

        //
        if (publication.toString() != "") {
            binding.btnPublicationTeacherModel.visibility = View.VISIBLE

            binding.btnPublicationTeacherModel.setOnClickListener {
                val url = publication
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

        }

        //
        binding.btnCallTeacherDetails.setOnClickListener {
            val call = "tel:$mobile"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(call)
            startActivity(intent)
            Toast.makeText(this, "Open Dialer", Toast.LENGTH_SHORT).show()
        }

        binding.btnMailTeacherDetails.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

            startActivity(Intent.createChooser(intent, "Email via..."))
        }

    }

}