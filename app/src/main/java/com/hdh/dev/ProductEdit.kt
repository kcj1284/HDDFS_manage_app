package com.hdh.dev

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.hdh.dev.databinding.ActivityProductEditBinding
import com.hdh.dev.databinding.ActivityProductListBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity
import java.io.File
import java.text.DecimalFormat

class ProductEdit : AppCompatActivity() {

    private lateinit var binding : ActivityProductEditBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao

    private lateinit var productEntity : ProductEntity//업데이트에 사용될놈 처음 화면로드될때 초기화시킴

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        // 상품 배치도 자세히보기
        binding.layout01.setOnClickListener {
            if(binding.layoutDetail01.visibility == View.VISIBLE) {
                binding.layoutDetail01.visibility = View.GONE
                binding.layoutBtn01.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            } else {
                binding.layoutDetail01.visibility = View.VISIBLE
                binding.layoutBtn01.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }
        // 상품 배치도 버튼 클릭 시, view에 값 저장
        binding.btnA1.setOnClickListener {
            binding.editLoction.setText("A-1")
        }
        binding.btnA2.setOnClickListener {
            binding.editLoction.setText("A-2")
        }
        binding.btnA3.setOnClickListener {
            binding.editLoction.setText("A-3")
        }
        binding.btnA4.setOnClickListener {
            binding.editLoction.setText("A-4")
        }
        binding.btnB1.setOnClickListener {
            binding.editLoction.setText("B-1")
        }
        binding.btnB2.setOnClickListener {
            binding.editLoction.setText("B-2")
        }
        binding.btnB3.setOnClickListener {
            binding.editLoction.setText("B-3")
        }
        binding.btnB4.setOnClickListener {
            binding.editLoction.setText("B-4")
        }
        binding.btnC1.setOnClickListener {
            binding.editLoction.setText("C-1")
        }
        binding.btnC2.setOnClickListener {
            binding.editLoction.setText("C-2")
        }
        binding.btnC3.setOnClickListener {
            binding.editLoction.setText("C-3")
        }
        binding.btnC4.setOnClickListener {
            binding.editLoction.setText("C-4")
        }
        binding.btnD1.setOnClickListener {
            binding.editLoction.setText("D-1")
        }
        binding.btnD2.setOnClickListener {
            binding.editLoction.setText("D-2")
        }
        binding.btnD3.setOnClickListener {
            binding.editLoction.setText("D-3")
        }
        binding.btnD4.setOnClickListener {
            binding.editLoction.setText("D-4")
        }


        getProductInfo()

        binding.editCompleteBtn.setOnClickListener {
            //바뀐게 있다면 정말 바꿀껀지 물어보는 창 띄우기!
            //바뀐게 없다면 아무것도 안 바뀌였다고 말해주기!
            ischanged()
        }
        binding.addCancleBtn.setOnClickListener {
            finish()
        }
    }

    private fun getProductInfo(){
        val pid = intent.getStringExtra("pid")
        val code = intent.getStringExtra("code")
        val image = intent.getStringExtra("image")
        val name = intent.getStringExtra("name")
        val category = intent.getStringExtra("category")
        val price = intent.getStringExtra("price")
        val location = intent.getStringExtra("location")
        val stock = intent.getStringExtra("stock")
        val did = intent.getStringExtra("did")

        productEntity = ProductEntity(pid!!.toInt(), code!!, name!!, image!!, category!!, price!!.toInt(), location!!, stock!!.toInt(), did!!.toInt())
        //이미지 파일 불러오기위함 !
        val photoFile = File(
            File("${filesDir}/image").apply {
                if(!this.exists()){
                    this.mkdirs()
                }
            },image
        )

        val photoUri = FileProvider.getUriForFile(
            this,
            "com.hdh.dev.fileprovider", //인증
            photoFile // 파일 저장될 경로 + 파일 이름
        )

        //가격 , 찍기
        val price_dec = DecimalFormat("#,###")
        val price_str = price_dec.format(price!!.toLong()).toString()

        binding.addedPicture.setImageURI(photoUri)
        binding.addName.text = name
        binding.editCategoty.text = category
        binding.editPrice.hint = price_str
        binding.editLoction.hint = location
        binding.editStockEdit.hint = stock
    }//end getProductInfo..

    private fun ischanged(){
        val c_price = binding.editPrice.text
        val c_location = binding.editLoction.text
        val c_stock = binding.editStockEdit.text

        //다이얼로그창 띄우기
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("상품정보수정")
        if(!c_price.isBlank() || !c_location.isBlank() || !c_stock.isBlank()){
            builder.setMessage("정말 수정하시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("수정", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    updateProduct()
                }
            })

            //productEntity 바꿔주기
            if(!c_price.isBlank()) {productEntity.price = c_price.toString().toInt()}
            if(!c_location.isBlank()) {productEntity.loction = c_location.toString()}
            if(!c_stock.isBlank()) {productEntity.stock = c_stock.toString().toInt()}
        }else{
            builder.setMessage("변동된게 없습니다")
            builder.setNegativeButton("확인", null)
        }
        builder.show()
    }

    private fun updateProduct(){
        Thread{
            productDao.updateProduct(productEntity)
            runOnUiThread {
                Toast.makeText(this@ProductEdit, "업데이트 완료", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}