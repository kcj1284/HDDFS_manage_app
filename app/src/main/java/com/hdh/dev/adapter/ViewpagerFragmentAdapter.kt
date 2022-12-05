package com.hdh.dev.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hdh.dev.db.ProductEntity
import com.hdh.dev.fragment.PlistFragment

class ViewpagerFragmentAdapter(
    fragmentActivity: FragmentActivity,
    categoryList: List<ArrayList<ProductEntity>>
    ) : FragmentStateAdapter(fragmentActivity) {

    private var fragmentList = ArrayList<PlistFragment>()

    init {
        categoryList.forEach {
            val productListFragment = PlistFragment(fragmentActivity, it)
            fragmentList.add(productListFragment)
        }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}