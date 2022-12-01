package com.hdh.dev.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hdh.dev.fragment.PlistFragment

class ViewpagerFragmentAdapter(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    val fragmentList = listOf<Fragment>(PlistFragment(), PlistFragment())

    override fun getItemCount(): Int {
        return fragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}