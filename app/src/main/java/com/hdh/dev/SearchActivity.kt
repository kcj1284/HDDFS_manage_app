package com.hdh.dev

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.adapter.ViewpagerFragmentAdapter
import com.hdh.dev.databinding.ActivitySearchBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao
    private lateinit var adapter : PlistFragmentRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        val sv = binding.searchView
        sv.isSubmitButtonEnabled = true
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색 버튼 누를 때 호출
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("gahee",query!!)
                search(query)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출

                return true
            }
        })

    }

    private fun search(pname : String){
        val productList = ArrayList<ArrayList<ProductEntity>>()
        Thread {
            Log.d("gahee", "검색함수들어옴")
            productList.add(productDao.searchProduct(pname) as ArrayList<ProductEntity>)
            Log.d("gahee", productList[0].toString())
        }.start()
        createFragmentAdapter(productList)
    }

    private fun createFragmentAdapter(productList : List<ArrayList<ProductEntity>>){
        runOnUiThread {
          //adapter = PlistFragmentRecyclerViewAdapter(productList)
        }
    }
}