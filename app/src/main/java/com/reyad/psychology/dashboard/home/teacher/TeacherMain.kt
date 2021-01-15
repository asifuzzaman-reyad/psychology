package com.reyad.psychology.dashboard.home.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.reyad.psychology.R


class TeacherMain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_teacher_main, container, false)

        val pagerAdapter = TeacherPagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(TeacherPresent(), "Present")
        pagerAdapter.addFragment(TeacherAbsent(), "Study Leave")

        val viewPager: ViewPager = view.findViewById(R.id.view_pager_teacher)
        viewPager.adapter = pagerAdapter

        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        return view
    }
}