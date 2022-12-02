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
    }
}