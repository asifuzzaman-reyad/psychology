package com.reyad.psychology.dashboard.home.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.reyad.psychology.R

class StudentMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        val pagerAdapter = StudentPagerAdapter(this, supportFragmentManager)
        pagerAdapter.addFragment(Batch15(), "15th Batch")
        pagerAdapter.addFragment(Batch14(), "14th Batch")
        pagerAdapter.addFragment(Batch13(), "13th Batch")
        pagerAdapter.addFragment(Batch12(), "12th Batch")

        val viewPager: ViewPager = findViewById(R.id.view_pager_student)
        viewPager.adapter = pagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)


    }
}