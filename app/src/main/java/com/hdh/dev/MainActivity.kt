package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hdh.dev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}