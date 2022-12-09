package com.hdh.dev

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hdh.dev.adapter.AnnounceRecyclerViewAdapter
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.databinding.ActivityAnnounceDetailBinding
import com.hdh.dev.db.AnnounceDao
import com.hdh.dev.db.AnnounceEntity
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductEntity


class AnnounceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnnounceDetailBinding
    private lateinit var db:AppDatabase
    private lateinit var announceDao:AnnounceDao

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAnnounceDetailBinding.inflate(layoutInflater)
        val announceData = intent.getSerializableExtra("announce") as AnnounceEntity

        //관리자모드일 때만 수정버튼 보이도록
        if (StartActivity.DEPARTMENT_INDEX == 3) {
        binding.bntAnnounceEdit.visibility = View.VISIBLE
        }

        binding.announceDetailSubject.text = announceData.annTitle.toString()
        binding.announceDetailContent.text = announceData.annContent.toString()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //수정버튼 눌리면 수정화면으로 이동
        binding.bntAnnounceEdit.setOnClickListener {
            val intentEdit = Intent(this, AnnounceEdit::class.java)
            intentEdit.putExtra("no",announceData.annNo!!.toInt())
            intentEdit.putExtra("title",announceData.annTitle.toString())
            intentEdit.putExtra("content",announceData.annContent.toString())
            startActivity(intentEdit)
        }

    }

    override fun onRestart() {
        super.onRestart()
        val intent = Intent(this, Announce::class.java)
        startActivity(intent)
    }
}