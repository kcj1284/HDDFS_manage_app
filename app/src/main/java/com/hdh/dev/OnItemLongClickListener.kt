package com.hdh.dev

import com.hdh.dev.db.ProductEntity

interface OnItemLongClickListener {
    fun onLongClick(productEntity : ProductEntity)
}