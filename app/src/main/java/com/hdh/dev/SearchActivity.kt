package com.hdh.dev

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnKeyListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.adapter.ViewpagerFragmentAdapter
import com.hdh.dev.databinding.ActivitySearchBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity


class SearchActivity : AppCompatActivity() , OnItemLongClickListener{
    private lateinit var binding: ActivitySearchBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao
    private lateinit var productList : ArrayList<ProductEntity>
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

    override fun onLongClick(position: Int, adapter: PlistFragmentRecyclerViewAdapter) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("상품삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("삭제", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                Thread{
                    productDao.deleteProduct(adapter.productList[position])//db에서 먼저 제품 지우고
                    adapter.productList.removeAt(position)//adapter에 서 가지고있는 list에서도 빼주기
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@SearchActivity, "삭제완료", Toast.LENGTH_SHORT).show()
                        //onRestart()//이런식으로 가면 버전낮은 애들은 팅긴다는데... 추가 방안을 찾아보자
                    }
                }.start()
            }
        })
        builder.show()
    }

    private fun search(query : String){
        val searchQuery = "%$query%"
        Thread {
            Log.d("gahee", "검색함수들어옴")
            productList = productDao.searchProduct(searchQuery,StartActivity.DEPARTMENT_INDEX) as ArrayList<ProductEntity>
            if(productList.size==0){
                Log.d("gahee", "검색결과없음")
            }else{
                setRecyclerView(productList)
                Log.d("gahee", productList[0].toString())
            }

        }.start()

    }

    private fun setRecyclerView(productList : ArrayList<ProductEntity>){
        runOnUiThread {
            adapter = PlistFragmentRecyclerViewAdapter(productList,this) // ❷ 어댑터 객체 할당
            binding.recyclerView.adapter = adapter // 리사이클러뷰 어댑터로 위에서 만든 어댑터 설정
            binding.recyclerView.layoutManager = LinearLayoutManager(this) // 레이아웃 매니저 설정
        }
    }

}