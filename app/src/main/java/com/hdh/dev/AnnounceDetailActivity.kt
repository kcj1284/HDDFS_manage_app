package com.hdh.dev

import android.content.DialogInterface
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

        binding.announceDetailSubject.text = announceData.annTitle.toString()
        binding.announceDetailContent.text = announceData.annContent.toString()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}