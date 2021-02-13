package com.reyad.psychology.dashboard.home.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.reyad.psychology.R


class TeacherMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_teacher_main)

        val pagerAdapter = TeacherPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(TeacherPresent(), "Present")
        pagerAdapter.addFragment(TeacherAbsent(), "Study Leave")

        val viewPager: ViewPager = findViewById(R.id.view_pager_teacher)
        viewPager.adapter = pagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }
}