package com.hdh.dev

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.hdh.dev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        /*Toolbar toolbar = (Toolbar)findViewById(binding.toolba);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.); //왼쪽 상단 버튼 아이콘 지정
*/
        binding.addItemMainBtn.setOnClickListener{
            val intentAddProduct = Intent(this, AddProduct::class.java)
            startActivity(intentAddProduct)
        }
        /*binding.searchItemMainBtn.setOnClickListener{
            val intentSearch = Intent(this, SearchProduct::class.java)
            startActivity(intentSearch)
        }*/
        /*binding.qrSearchItemMainBtn.setOnClickListener{
            val intentQrSearch = Intent(this, QrSearchProduct::class.java)
            startActivity(intentQrSearch)
        }*/
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

    //왼쪽 스와이프하면 툴바열리기
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }*/

    //뒤로가기 했을 때 네비게이션바닫히기
    override fun onBackPressed() { 
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}