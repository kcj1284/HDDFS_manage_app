package com.hdh.dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.hdh.dev.databinding.ActivitySelectDepartmentBinding

class SelectDepartment : AppCompatActivity() {

    private lateinit var binding: ActivitySelectDepartmentBinding
    val departmentList = arrayOf("삼성점", "강남점", "판교점")
    private var DEPARTMENT_INDEX = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectDepartment.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, departmentList)
        binding.selectDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                DEPARTMENT_INDEX = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.selectDepartmentBtn.setOnClickListener{
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }
    }
}