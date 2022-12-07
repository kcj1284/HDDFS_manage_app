package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StartLogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_logo)

        val nextIntent = Intent(this, StartActivity::class.java)
        startActivity(nextIntent)
        finish()
    }
}