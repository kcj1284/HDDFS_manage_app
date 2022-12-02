package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.hdh.dev.databinding.ActivityAnnounceBinding

class Announce : AppCompatActivity() {

    private lateinit var binding: ActivityAnnounceBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAnnounceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.announce01Btn.setOnClickListener{
            val intentAnnounce01 = Intent(this, AnnounceContent01::class.java)
            startActivity(intentAnnounce01)
        }

        binding.announce02Btn.setOnClickListener {
            val intentAnnounce02 = Intent(this, AnnounceContent02::class.java)
            startActivity(intentAnnounce02)
        }

        binding.announce03Btn.setOnClickListener {
            val intentAnnounce03 = Intent(this, AnnounceContent03::class.java)
            startActivity(intentAnnounce03)
        }

        binding.announce04Btn.setOnClickListener {
            val intentAnnounce04 = Intent(this, AnnounceContent04::class.java)
            startActivity(intentAnnounce04)
        }
    }
}