package com.hdh.dev

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.hdh.dev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //툴바설정
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setDisplayShowTitleEnabled(false)    // 타이틀 안보이게 하기
        binding.navigationView.setNavigationItemSelectedListener(this)  //네비게이션뷰 리스너 등록

        val departmentList = arrayOf("무역센터점", "목동점", "천호점","관리자모드")
        binding.deparmentBranch.text = departmentList[StartActivity.DEPARTMENT_INDEX]//어느 지점인지 출력

        //네비게이션 헤더에 지점명 출력
        val headerTxt = binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.branch)
        headerTxt.setText(departmentList[StartActivity.DEPARTMENT_INDEX])
        //네비게이션 헤더에 지점사진 출력
        val headerImg = binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_image)
        when(StartActivity.DEPARTMENT_INDEX){
            1 ->  headerImg.setImageDrawable(getResources().getDrawable(R.drawable.mokdong))
            2 ->  headerImg.setImageDrawable(getResources().getDrawable(R.drawable.cheonho))
            3 ->  headerImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round))
        }

        binding.addItemMainBtn.setOnClickListener{
            val intentAddProduct = Intent(this, AddProduct::class.java)
            startActivity(intentAddProduct)
        }
        binding.searchItemMainBtn.setOnClickListener{
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }
        binding.qrSearchItemMainBtn.setOnClickListener{
            val intentQrSearch = Intent(this, QrSearch::class.java)
            startActivity(intentQrSearch)
        }
        binding.stockItemMainBtn.setOnClickListener{
            val intentStock = Intent(this, ProductList::class.java)
            startActivity(intentStock)
        }
        binding.settingMainBtn.setOnClickListener{
            val intentSetting = Intent(this, SetApp::class.java)
            startActivity(intentSetting)
        }
        binding.announcementItemMainBtn.setOnClickListener{
            val intentAnnounce = Intent(this, Announce::class.java)
            startActivity(intentAnnounce)
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