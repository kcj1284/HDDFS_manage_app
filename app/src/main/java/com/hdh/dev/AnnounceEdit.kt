package com.hdh.dev

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hdh.dev.databinding.ActivityAnnounceDetailBinding
import com.hdh.dev.databinding.ActivityAnnounceEditBinding
import com.hdh.dev.db.AnnounceDao
import com.hdh.dev.db.AnnounceEntity
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductEntity

class AnnounceEdit : AppCompatActivity() {

    private lateinit var binding: ActivityAnnounceEditBinding
    private lateinit var db: AppDatabase
    private lateinit var announceDao: AnnounceDao

    private lateinit var annEntity : AnnounceEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnounceEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        announceDao = db.AnnounceDao()

        //val announceData = intent.getSerializableExtra("announce")
        val annNo = intent.getIntExtra("no",0)
        val annTitle = intent.getStringExtra("title")
        val annContent = intent.getStringExtra("content")
        annEntity = AnnounceEntity(annNo, annTitle.toString()!!,annContent.toString()!!)
        Log.d("gahee", annTitle.toString())
        Log.d("gahee", annContent.toString())

        //원래 입력되었던 값 불러오기
        binding.editSubject.setText(annTitle.toString())
        binding.editContent.setText(annContent.toString())

        binding.editCompleteBtn.setOnClickListener {
            //바뀐게 있는지 체크
            ischanged()
        }
        binding.addCancleBtn.setOnClickListener {
            finish()
        }


    }

    private fun ischanged(){
        val c_title = binding.editSubject.text
        val c_content = binding.editContent.text

        //다이얼로그창 띄우기
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("공지사항수정")
        if(!c_title.isBlank() || !c_content.isBlank()){
            builder.setMessage("정말 수정하시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("수정", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    update()
                }
            })

            //AnnounceEntity 바꿔주기
            if(!c_title.isBlank()) {annEntity.annTitle = c_title.toString()}
            if(!c_content.isBlank()) {annEntity.annContent = c_content.toString()}
        }else{
            builder.setMessage("변동된게 없습니다")
            builder.setNegativeButton("확인", null)
        }
        builder.show()
    }

    private fun update(){
        Thread{
            announceDao.updateAnnounce(annEntity)
            runOnUiThread {
                Toast.makeText(this@AnnounceEdit, "업데이트 완료", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }


}