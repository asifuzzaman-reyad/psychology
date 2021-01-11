@file:Suppress("DEPRECATION")

package com.reyad.psychology.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.reyad.psychology.databinding.ActivityMessengerBinding
import com.reyad.psychology.messenger.users.AllUser

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