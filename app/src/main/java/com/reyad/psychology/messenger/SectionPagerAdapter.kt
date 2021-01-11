@file:Suppress("DEPRECATION")

package com.reyad.psychology.messenger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()

    //item count
    override fun getCount(): Int {
        return titleList.size
    }

    // get fragment list
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    // get title list
    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }

    // add fragment abd title on array
    fun addFragmentAndTitle(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }


}
