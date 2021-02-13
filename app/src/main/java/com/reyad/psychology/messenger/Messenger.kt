@file:Suppress("DEPRECATION")

package com.reyad.psychology.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.reyad.psychology.R
import com.reyad.psychology.databinding.ActivityMessengerBinding
import com.reyad.psychology.messenger.users.AllUser
import com.reyad.psychology.register.user.Profile
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class Messenger : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding
    private lateinit var viewPager: ViewPager
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //
        val batch  = intent.getStringExtra("batch").toString()
        val id  = intent.getStringExtra("id").toString()
        val name  = intent.getStringExtra("name").toString()
        val imageUrl  = intent.getStringExtra("imageUrl").toString()

        //
        binding.tvNameMessenger.text = name

        //
        if (imageUrl.isNotEmpty()) {
            Picasso.get().load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.male_avatar)
                .into(binding.civProfileMessenger, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Picasso.get().load(imageUrl).placeholder(R.drawable.male_avatar)
                            .into(binding.civProfileMessenger)
                    }
                })
        }

        //

        // button profile
        binding.civProfileMessenger.setOnClickListener {

            val profileIntent = Intent(this, Profile::class.java).apply {
                putExtra("batch", batch.toString())
                putExtra("id", id.toString())
            }
            startActivity(profileIntent)
        }

        //hooks
        viewPager = binding.viewPagerMessenger
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        tabLayout = binding.tabLayoutMessenger

        //pager adapter
        sectionPagerAdapter.addFragmentAndTitle(FragmentFirst(), "Chat")
        sectionPagerAdapter.addFragmentAndTitle(FragmentSecond(), "Friends")
        sectionPagerAdapter.addFragmentAndTitle(FragmentThird(), "Request")

        //add adapter with viewpager
        viewPager.adapter = sectionPagerAdapter

        //add viewpager with tab layout
        tabLayout.setupWithViewPager(viewPager)

        //fab
        binding.fabAllUserMessenger.setOnClickListener {
            val allUserIntent = Intent(this, AllUser::class.java)
            startActivity(allUserIntent)
        }

    }
}