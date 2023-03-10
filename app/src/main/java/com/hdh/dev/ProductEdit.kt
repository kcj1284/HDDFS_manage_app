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

    private lateinit var productEntity : ProductEntity//업데이트에 사용될 ProductEntity 처음 화면로드될때 초기화시킴

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        binding.tvItemStock.setOnClickListener {
            if(binding.otherDepartmentLayout.visibility == View.VISIBLE){
                binding.otherDepartmentLayout.visibility = View.GONE
            }else{
                binding.otherDepartmentLayout.visibility = View.VISIBLE
            }
        }
        getOtherDepartmentStock(StartActivity.DEPARTMENT_INDEX)
        getProductInfo()

        binding.editCompleteBtn.setOnClickListener {
            //바뀐게 있다면 정말 바꿀껀지 물어보는 창 띄우기!
            //바뀐게 없다면 아무것도 안 바뀌였다고 말해주기!
            ischanged()
        }
        binding.addCancleBtn.setOnClickListener {
            finish()
        }

        // qr코드 자세히보기
        binding.addqrImagebtn.setOnClickListener {
            if(binding.layout01.visibility == View.VISIBLE) {
                binding.layout01.visibility = View.GONE
            } else {
                binding.layout01.visibility = View.VISIBLE
            }
        }
    }

    private fun getOtherDepartmentStock(curDepartmentIndex: Int){
        val departmentList = mutableListOf<String>("무역센터점", "목동점", "천호점")
        val didList = mutableListOf<Int>(0,1,2)

        val pcode = intent.getStringExtra("pcode")

        departmentList.removeAt(curDepartmentIndex) //지금 백화점 지점을 목록에서 지워주기
        didList.removeAt(curDepartmentIndex) // 현 did말고 나머지만 남기기
        Thread{
            val productEntity1 = productDao.getDepartmentProductStock(pcode!!, didList[0])
            val productEntity2 = productDao.getDepartmentProductStock(pcode!!, didList[1])
            runOnUiThread {
                binding.otherDepartmentBranch1.text = departmentList[0]
                binding.otherDepartmentBranch1Stock.text = if(productEntity1.size == 0) "아직 입고가 안됐어요" else productEntity1[0].stock.toString()
                binding.otherDepartmentBranch2.text = departmentList[1]
                binding.otherDepartmentBranch2Stock.text = if(productEntity2.size == 0) "아직 입고가 안됐어요" else productEntity2[0].stock.toString()
            }
        }.start()
    }
    private fun getProductInfo(){
        val pid = intent.getStringExtra("pid")
        val pcode = intent.getStringExtra("pcode")
        val image = intent.getStringExtra("image")
        val name = intent.getStringExtra("name")
        val category = intent.getStringExtra("category")
        val price = intent.getStringExtra("price")
        val location = intent.getStringExtra("location")
        val stock = intent.getStringExtra("stock")
        val did = intent.getStringExtra("did")

        productEntity = ProductEntity(pid!!.toInt(), pcode!!, name!!, image!!, category!!, price!!.toInt(), location!!, stock!!.toInt(), did!!.toInt())
        //이미지 파일 불러오기위함 !
        val photoFile = File(
            File("${filesDir}/image").apply {
                if(!this.exists()){
                    this.mkdirs()
                }
            },image

        )
        val qr_photoFile = File(
            File("${filesDir}/qr_image").apply {
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
        val qr_photoUri = FileProvider.getUriForFile(
            this,
            "com.hdh.dev.fileprovider", //인증
            qr_photoFile // 파일 저장될 경로 + 파일 이름
        )

        //가격 , 찍기
        val price_dec = DecimalFormat("#,###")
        val price_str = price_dec.format(price!!.toLong()).toString()

        binding.editPcode.text = pcode
        binding.addedPicture.setImageURI(photoUri)
        binding.addName.text = name
        binding.editCategoty.text = category
        binding.editPrice.hint = price_str
        binding.editLoction.hint = location
        binding.editStockEdit.hint = stock
        binding.qrPicture.setImageURI(qr_photoUri)
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