package com.hdh.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.hdh.dev.databinding.ActivitySetAppBinding

class SetApp : AppCompatActivity() {

    private lateinit var binding: ActivitySetAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lightModeBtn.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        binding.darkModeBtn.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}