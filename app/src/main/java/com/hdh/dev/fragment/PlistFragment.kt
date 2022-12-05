package com.hdh.dev.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdh.dev.OnItemLongClickListener
import com.hdh.dev.R
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.databinding.FragmentProductListBinding
import com.hdh.dev.db.ProductEntity

class PlistFragment(
    val context : FragmentActivity,
    val productList: ArrayList<ProductEntity>) : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlistFragmentRecyclerViewAdapter(productList, context as OnItemLongClickListener)
        val binding = FragmentProductListBinding.inflate(layoutInflater)

        view.findViewById<RecyclerView>(R.id.productList_recyclerView).layoutManager = LinearLayoutManager(context)
        view.findViewById<RecyclerView>(R.id.productList_recyclerView).adapter = adapter
        //binding.productListRecyclerView.layoutManager = LinearLayoutManager(context)
        //binding.productListRecyclerView.adapter = adapter
    }

}