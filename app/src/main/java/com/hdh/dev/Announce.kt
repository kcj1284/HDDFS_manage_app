package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.hdh.dev.databinding.ActivityAnnounceBinding

class Announce : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAnnounceBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAnnounceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바설정
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 안보이게 하기
        binding.navigationView.setNavigationItemSelectedListener(this)


        binding.announce01Btn.setOnClickListener{
            val intentAnnounce01 = Intent(this, AnnounceContent::class.java)
            intentAnnounce01.putExtra("order", 1)
            startActivity(intentAnnounce01)
        }

        binding.announce02Btn.setOnClickListener {
            val intentAnnounce02 = Intent(this, AnnounceContent::class.java)
            intentAnnounce02.putExtra("order", 2)
            startActivity(intentAnnounce02)
        }

        binding.announce03Btn.setOnClickListener {
            val intentAnnounce03 = Intent(this, AnnounceContent::class.java)
            intentAnnounce03.putExtra("order", 3)
            startActivity(intentAnnounce03)
        }

        binding.announce04Btn.setOnClickListener {
            val intentAnnounce04 = Intent(this, AnnounceContent::class.java)
            intentAnnounce04.putExtra("order", 4)
            startActivity(intentAnnounce04)
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
        Log.d("gahee","버튼이눌렷다아아아아 $item")
        when(item.itemId){
            R.id.home_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentHome = Intent(this, MainActivity::class.java)
                startActivity(intentHome)
            }
            R.id.add_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentAddProduct = Intent(this, AddProduct::class.java)
                startActivity(intentAddProduct)
            }
            R.id.search_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentSearch = Intent(this, SearchActivity::class.java)
                startActivity(intentSearch)
            }
            R.id.qrSearch_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentQrSearch = Intent(this, QrSearch::class.java)
                startActivity(intentQrSearch)
            }
            R.id.stock_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentStock = Intent(this, ProductList::class.java)
                startActivity(intentStock)
            }
            R.id.setting_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentSetting = Intent(this, SetApp::class.java)
                startActivity(intentSetting)
            }
            R.id.announcement_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentAnnounce = Intent(this, Announce::class.java)
                startActivity(intentAnnounce)
            }
        }
        binding.drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }
}