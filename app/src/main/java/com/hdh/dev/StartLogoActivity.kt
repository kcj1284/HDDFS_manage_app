package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class StartLogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_logo)

/*        val nextIntent = Intent(this, StartActivity::class.java)
        startActivity(nextIntent)
        finish()*/

        Handler(Looper.getMainLooper()).postDelayed({

            // 일정 시간이 지나면 StartActivity 로 이동
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)

            finish()

        }, 2000) // 2초 이후 실행
    }

}