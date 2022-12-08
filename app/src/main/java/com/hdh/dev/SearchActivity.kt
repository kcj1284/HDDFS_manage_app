package com.hdh.dev

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.adapter.ViewpagerFragmentAdapter
import com.hdh.dev.databinding.ActivitySearchBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity


class SearchActivity : AppCompatActivity() , OnItemLongClickListener, NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivitySearchBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao
    private lateinit var productList : ArrayList<ProductEntity>
    private lateinit var adapter : PlistFragmentRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.navigationView.setNavigationItemSelectedListener(this)

        setContentView(binding.root)
        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        val sv = binding.searchView
        sv.isSubmitButtonEnabled = true

        //툴바설정
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀
        binding.navigationView.setNavigationItemSelectedListener(this)

        val departmentList = arrayOf("강남점", "목동점", "삼성점")
        //네비게이션 헤더
        val header = binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.branch)
        header.setText(departmentList[StartActivity.DEPARTMENT_INDEX])

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색 버튼 누를 때 호출
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("gahee",query!!)
                search(query)

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출
                if(query != null){
                    search(query)
                }
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
                runOnUiThread {
                    binding.tvNoResult.visibility = VISIBLE
                    binding.recyclerView.visibility = INVISIBLE
                }
            }else{
                runOnUiThread {
                    binding.tvNoResult.visibility = INVISIBLE
                    binding.recyclerView.visibility = VISIBLE
                }
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

    //메뉴 아이콘 누르면 네비게이션바열리기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //뒤로가기 했을 때 네비게이션바닫히기
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //네비게이션바에서 메뉴이동하기
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentHome = Intent(this, MainActivity::class.java)
                startActivity(intentHome)
            }
            R.id.add_item_menu_btn->{
                val intentAddProduct = Intent(this, AddProduct::class.java)
                startActivity(intentAddProduct)
            }
            R.id.search_item_menu_btn->{
                val intentSearch = Intent(this, SearchActivity::class.java)
                startActivity(intentSearch)
            }
            R.id.qrSearch_item_menu_btn->{
                val intentQrSearch = Intent(this, QrSearch::class.java)
                startActivity(intentQrSearch)
            }
            R.id.stock_item_menu_btn->{
                val intentStock = Intent(this, ProductList::class.java)
                startActivity(intentStock)
            }
            R.id.setting_menu_btn->{
                val intentSetting = Intent(this, SetApp::class.java)
                startActivity(intentSetting)
            }
            R.id.announcement_item_menu_btn->{
                val intentAnnounce = Intent(this, Announce::class.java)
                startActivity(intentAnnounce)
            }
        }
        binding.drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }

}