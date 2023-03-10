package com.hdh.dev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.hdh.dev.databinding.ActivitySelectDepartmentBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.DepartmentDao
import com.hdh.dev.db.DepartmentEntity

class StartActivity : AppCompatActivity() {

    companion object{
        lateinit var context_start : Context//App내에서 전역적으로 DEPARTMENT_INDEX를 사용하기위한 변수
        var DEPARTMENT_INDEX = 0
    }

    private lateinit var binding: ActivitySelectDepartmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context_start = this// App내에서 전역적으로 사용할 변수를 위한 Context 초기화

        val departmentList = arrayOf("무역센터점", "목동점", "천호점", "관리자모드")
        singletonDepartmentEntity()

        binding.selectDepartment.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, departmentList)
        binding.selectDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                DEPARTMENT_INDEX = position
                //선택 지점에 따라 배경이미지 바꾸기
                val bgImg = binding.ivBg
                when(DEPARTMENT_INDEX){
                    0 -> bgImg.setImageDrawable(getResources().getDrawable(R.drawable.startbg))
                    1 ->  bgImg.setImageDrawable(getResources().getDrawable(R.drawable.startbg1))
                    2 ->  bgImg.setImageDrawable(getResources().getDrawable(R.drawable.startbg2))
                    3 ->  bgImg.setImageDrawable(getResources().getDrawable(R.drawable.startbg3))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.selectDepartmentBtn.setOnClickListener{
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }
    }//end onCreate...

    private fun singletonDepartmentEntity( ){
        val db : AppDatabase = AppDatabase.getInstance(this)!!
        val dao : DepartmentDao = db.getDepartmentDao()

        Thread{
            val count = dao.getDepartment().size
            if(count == 0){//아직 지점Entitiy가 생성이 안됐으므로 생성해주기 -> 어플리케이션이 처음 실행 되었을때를 위함
                dao.insertDepartment(DepartmentEntity(0, "무역센터점"))
                dao.insertDepartment(DepartmentEntity(1, "목동점"))
                dao.insertDepartment(DepartmentEntity(2, "천호점"))
                dao.insertDepartment(DepartmentEntity(10, "관리자모드"))
            }
        }.start()
    }
}