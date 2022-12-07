package com.hdh.dev

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.hdh.dev.databinding.ActivityAddProductBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity
import java.io.File
import java.io.FileOutputStream

class AddProduct : AppCompatActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao

    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    //사진 찍어서 저장할 용도의 변수 -> 사진저장에 사용
    //할당을 안하면 null이므로 유의!
    private lateinit var bitmap : Bitmap//아마도 미리보기
    private var photoUri: Uri? = null//원본 사진이 저장되는 Uri

    private val categoryList = arrayOf("상의", "하의", "잡화")
    private var CATEGORY_INDEX = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        // 상품 배치도 자세히보기
        binding.addLoctionBtn.setOnClickListener {
            if(binding.layout01.visibility == View.VISIBLE) {
                binding.layout01.visibility = View.GONE
            } else {
                binding.layout01.visibility = View.VISIBLE
            }
        }
        // 상품 배치도 버튼 클릭 시, view에 값 저장
        binding.btnA1.setOnClickListener {
            binding.addLoction.setText("A-1")
        }
        binding.btnA2.setOnClickListener {
            binding.addLoction.setText("A-2")
        }
        binding.btnA3.setOnClickListener {
            binding.addLoction.setText("A-3")
        }
        binding.btnA4.setOnClickListener {
            binding.addLoction.setText("A-4")
        }
        binding.btnB1.setOnClickListener {
            binding.addLoction.setText("B-1")
        }
        binding.btnB2.setOnClickListener {
            binding.addLoction.setText("B-2")
        }
        binding.btnB3.setOnClickListener {
            binding.addLoction.setText("B-3")
        }
        binding.btnB4.setOnClickListener {
            binding.addLoction.setText("B-4")
        }
        binding.btnC1.setOnClickListener {
            binding.addLoction.setText("C-1")
        }
        binding.btnC2.setOnClickListener {
            binding.addLoction.setText("C-2")
        }
        binding.btnC3.setOnClickListener {
            binding.addLoction.setText("C-3")
        }
        binding.btnC4.setOnClickListener {
            binding.addLoction.setText("C-4")
        }
        binding.btnD1.setOnClickListener {
            binding.addLoction.setText("D-1")
        }
        binding.btnD2.setOnClickListener {
            binding.addLoction.setText("D-2")
        }
        binding.btnD3.setOnClickListener {
            binding.addLoction.setText("D-3")
        }
        binding.btnD4.setOnClickListener {
            binding.addLoction.setText("D-4")
        }

        //dropbox의 목록을 채우기위한 로직...

        binding.addCategoty.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, categoryList)

        //ArrayAdapter.createFromResource(this, R.array.product_category, R.layout.activity_add_product)
        binding.addCategoty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                CATEGORY_INDEX = position
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }//end of dropbox setting

        //startActivityFromResult가 depreted가 되어서 이객체를 사용해서 액티비티 호출 후 되돌아오는거까지
        val activityLauncher = openActivityResultLauncher()

        //사진추가버튼
        binding.addPictureBtn.setOnClickListener {
            //제품번호 입력안했으면 사진 선택 안되게!
            if (binding.addPcode.text.toString().isBlank()){
                Toast.makeText(this@AddProduct, "제품번호를 꼭 입력하세요!", Toast.LENGTH_SHORT).show()
            }else{
                if(!hasPermissions(this)){
                    requestMultiplePermission.launch(permissionList)//permissionList목록들의 권한을 받기
                }else{
                    //저장할 파일 이름 -> pcode로 이미지 저장
                    //차후 최종때는 확장자 jfif로 변경 필요
                    val imageFileName : String = binding.addPcode.text.toString()+".jpg"
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val photoFile = File(
                        File("${filesDir}/image").apply {
                            if(!this.exists()){
                                this.mkdirs()
                            }
                        },imageFileName

                    )

                    photoUri = FileProvider.getUriForFile(
                        this,
                        "com.hdh.dev.fileprovider", //인증
                        photoFile // 파일 저장될 경로 + 파일 이름
                    )

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)//이런식으로 intent에 Uri줘서 알아서 저장하게하면 미리보기 비트맵이 안오는것 같음
                    activityLauncher.launch(intent)//사진찍기. 다찍으면 아래 openActivityResultLauncher 실행해서 결과에따른 실행을 함

                }
            }

        }

        binding.addCancleBtn.setOnClickListener{
            val intentCancle = Intent(this, MainActivity::class.java)
            startActivity(intentCancle)
        }
        //재고추가버튼
        binding.addCompleteBtn.setOnClickListener {
            imageExternalSave()
            insertTodoList()
        }


    }//end onCreate..

    //사진찍고 돌아와서 수행할 메소드(자동실행)
    private fun openActivityResultLauncher(): ActivityResultLauncher<Intent>{
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->

            if(result.resultCode == Activity.RESULT_OK && result.data != null){
                Toast.makeText(this@AddProduct, "카메라 잘 찍었습니다.", Toast.LENGTH_SHORT).show()
                Log.i("files위치!", filesDir.toString())
                //val extras = result.data!!.extras
                //bitmap = extras?.get("data") as Bitmap
                //binding.addedPicture.setImageBitmap(bitmap)

                binding.addedPicture.setImageURI(photoUri)
                binding.addPictureBtn.visibility = View.GONE
            }else{
                Toast.makeText(this@AddProduct, "카메라 못써 ^^", Toast.LENGTH_SHORT).show()
            }
        }
        return resultLauncher
    }
    // 권한 요청
    private val requestMultiplePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.forEach {
            if(!it.value) {
                Toast.makeText(applicationContext, "${it.key} 권한 허용 필요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    //권한이 다 있는지 체크
    private fun hasPermissions(context : Context) = permissionList.all{
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    //재고추가 버튼 눌렀을때 빈칸이 있다면 다 채우라는 메시지띄우고 아니면 db에 저장
    private fun insertTodoList(){
        if(binding.addPcode.text.toString().isBlank() ||
            binding.addPrice.text.toString().isBlank() ||
            binding.addLoction.text.toString().isBlank() ||
            binding.addStock.text.toString().isBlank()
        ){
            Toast.makeText(this@AddProduct, "항목을 모두 채워주세요", Toast.LENGTH_SHORT).show()
        }else{
            //db저장
            Thread{
                productDao.insertProduct(ProductEntity(
                    null,
                    binding.addPcode.text.toString(), //pcode
                    binding.addName.text.toString(), //제품이름
                    binding.addPcode.text.toString() + ".jpg", // 제품이미지
                    categoryList[CATEGORY_INDEX], // 제품 카테고리
                    binding.addPrice.text.toString().toInt(), // 제품가격
                    binding.addLoction.text.toString(), // 제품위치
                    binding.addStock.text.toString().toInt(), // 제품재고
                    StartActivity.DEPARTMENT_INDEX
                ))
                runOnUiThread {
                    Toast.makeText(this@AddProduct, "추가 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }

    private fun imageExternalSave(): Boolean {

        val qrCode = QRCodeWriter()
        val bitMtx = qrCode.encode(binding.addPcode.text.toString(),
            BarcodeFormat.QR_CODE,
            1000,
            1000
        )
        //bitmap 생성
        val bitmap: Bitmap = Bitmap.createBitmap(bitMtx.width, bitMtx.height, Bitmap.Config.RGB_565)
        for(i in 0 .. bitMtx.width-1){
            for(j in 0 .. bitMtx.height-1){
                var color = 0
                if(bitMtx.get(i, j)){
                    color = Color.BLACK
                }else{
                    color = Color.WHITE
                }
                bitmap.setPixel(i, j, color)
            }
        }

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {

            val fileName = binding.addPcode.text.toString() + ".jfif"
            val file = File("${filesDir}/qr_image", fileName)
            if (file.exists()) file.delete()

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }
}