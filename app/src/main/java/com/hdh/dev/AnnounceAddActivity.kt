package com.hdh.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hdh.dev.databinding.ActivityAddProductBinding
import com.hdh.dev.databinding.ActivityAnnounceAddBinding
import com.hdh.dev.databinding.ActivityAnnounceBinding
import com.hdh.dev.db.AnnounceDao
import com.hdh.dev.db.AnnounceEntity
import com.hdh.dev.db.AppDatabase

class AnnounceAddActivity : AppCompatActivity() {

    lateinit var binding : ActivityAnnounceAddBinding
    lateinit var db : AppDatabase
    lateinit var announceDao: AnnounceDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnnounceAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        announceDao = db.AnnounceDao()

        binding.announceAddCompleteBtn.setOnClickListener {
            insertAnnounce()
        }
        binding.announceAddCancleBtn.setOnClickListener{
            finish()
        }
    }

    /* 공지 추가 함수 */
    private fun insertAnnounce() {
        val announceTitle = binding.announceAddSubject.text.toString()
        val announceContent = binding.announceAddContent.text.toString()

        if(announceTitle.isBlank() || announceContent.isBlank()) {
            Toast.makeText(this,"모든 항목을 채워주세요", Toast.LENGTH_SHORT).show()
        } else {
            Thread{
                ////productDao.insertProduct(ProductEntity(null, productName, productPrice,0,"","내용"))
                announceDao.insertAnnounce(AnnounceEntity(null, announceTitle, announceContent))

                runOnUiThread {
                    Toast.makeText(this,"공지가 등록되었습니다." ,
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }


}