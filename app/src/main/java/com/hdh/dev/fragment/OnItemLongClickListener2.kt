package com.hdh.dev.fragment

import com.hdh.dev.adapter.AnnounceRecyclerViewAdapter
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.db.AnnounceEntity
import com.hdh.dev.db.ProductEntity

interface OnItemLongClickListener2 {
    fun onLongClick2(position: Int , adapter: AnnounceRecyclerViewAdapter)
}