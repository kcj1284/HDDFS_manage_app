package com.hdh.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.hdh.dev.adapter.ViewpagerFragmentAdapter

class ProductList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val viewPager : ViewPager2 = findViewById(R.id.viewPager)

        val viewpagerFragmentAdapter = ViewpagerFragmentAdapter(this)

        viewPager.adapter = viewpagerFragmentAdapter
    }
}