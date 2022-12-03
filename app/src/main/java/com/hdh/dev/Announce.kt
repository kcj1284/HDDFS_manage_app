package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hdh.dev.databinding.ActivityAnnounceBinding

class Announce : AppCompatActivity() {

    private lateinit var binding: ActivityAnnounceBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAnnounceBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
}