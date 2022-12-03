package com.hdh.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AnnounceContent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val order = intent.getIntExtra("order", 0)

        when(order){
            1->setContentView(R.layout.activity_announce_content01)
            2->setContentView(R.layout.activity_announce_content02)
            3->setContentView(R.layout.activity_announce_content03)
            4->setContentView(R.layout.activity_announce_content04)
            else -> finish()
        }



    }
}

