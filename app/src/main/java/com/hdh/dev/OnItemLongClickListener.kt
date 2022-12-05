package com.hdh.dev

import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.db.ProductEntity

interface OnItemLongClickListener {
    fun onLongClick(position: Int , adapter: PlistFragmentRecyclerViewAdapter)
}