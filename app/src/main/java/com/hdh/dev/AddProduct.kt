package com.hdh.dev

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.hdh.dev.databinding.ActivityAddProductBinding
import com.hdh.dev.databinding.ActivityMainBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity
import java.io.File

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

    val categoryList = arrayOf("상의", "하의", "잡화")
    private var CATEGORY_INDEX = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()


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
            if (binding.addPid.text.toString().isBlank()){
                Toast.makeText(this@AddProduct, "제품번호를 꼭 입력하세요!", Toast.LENGTH_SHORT).show()
            }else{
                if(!hasPermissions(this)){
                    requestMultiplePermission.launch(permissionList)//permissionList목록들의 권한을 받기
                }else{
                    //저장할 파일 이름 -> pid로 이미지 저장
                    val imageFileName : String = binding.addPid.text.toString()+".jpg"
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
            var intentCancle = Intent(this, MainActivity::class.java)
            startActivity(intentCancle)
        }

        //재고추가버튼
        binding.addCompleteBtn.setOnClickListener {
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
        if(binding.addPid.text.toString().isBlank() ||
            binding.addPrice.text.toString().isBlank() ||
            binding.addLoction.text.toString().isBlank() ||
            binding.addStock.text.toString().isBlank()
            ){
            Toast.makeText(this@AddProduct, "항목을 모두 채워주세요", Toast.LENGTH_SHORT).show()
        }else{
            //db저장
            Thread{
                productDao.insertProduct(ProductEntity(
                    binding.addPid.text.toString(), //pid
                    binding.addName.text.toString(), //제품이름
                    binding.addPid.text.toString() + ".jpg", // 제품이미지
                    categoryList[CATEGORY_INDEX], // 제품 카테고리
                    binding.addPrice.text.toString().toInt(), // 제품가격
                    binding.addLoction.text.toString(), // 제품위치
                    binding.addStock.text.toString().toInt(), // 제품재고
                ))
                runOnUiThread {
                    Toast.makeText(this@AddProduct, "추가 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }

//        val partyRoomName = roomName.text.toString()
//        if(partyRoomName.isBlank() || todoList.childCount == 0 || restoreList.childCount == 0){
//            Toast.makeText(this@AddCheckListActivity, "항목 채워라", Toast.LENGTH_SHORT).show()
//        }else{
//            val insertList = ArrayList<RoomEntity>()
//            for(i in 0 until todoList.childCount){
//                val workList = todoList.getChildAt(i).findViewById<EditText>(R.id.do_work).text.toString()
//                val dataRow = RoomEntity(roomname = partyRoomName, type = 0, list = workList, quantity = -1)
//                insertList.add(dataRow)
//            }
//            for(i in 0 until restoreList.childCount){
//                val dataRow = RoomEntity(roomname = partyRoomName, type = 1, list = restoreList.getChildAt(i).findViewById<EditText>(R.id.do_work).text.toString(), quantity = 1)
//                insertList.add(dataRow)
//            }
//
//            Thread{
//                roomDao.insertTodoList(insertList)
//                runOnUiThread {
//                    Toast.makeText(this@AddCheckListActivity, "잘 들어갔어요", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }.start()
//        }
    }

}