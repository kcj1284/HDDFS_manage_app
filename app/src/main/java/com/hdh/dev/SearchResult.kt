package com.hdh.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hdh.dev.databinding.ActivitySearchResultBinding

class SearchResult : AppCompatActivity() {

    lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra("msg")?:"상품이 존재하지 않습니다."
        setUI(result)
    }
    private fun setUI(result: String) {
        binding.tvContent.text = result // 넘어온 QR 코드 속 데이터를 텍스트뷰에 설정합니다.
        binding.btnGoBack.setOnClickListener {
            finish() // 돌아가기 버튼을 눌러줬을 때 ResultActivity를 종료합니다.
        }
    }
}