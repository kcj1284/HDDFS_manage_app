package com.hdh.dev

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.hdh.dev.databinding.ActivityAddProductBinding
import com.hdh.dev.databinding.ActivityMainBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao

class AddProduct : AppCompatActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao

    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        binding.addPictureBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

//            registerForActivityResult()
            if(!hasPermissions(this)){
                Toast.makeText(this@AddProduct, "권한 해라 ㅡㅡ", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@AddProduct, "전부 다 됐어용", Toast.LENGTH_SHORT).show()
            }

        }

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

    //
    private fun hasPermissions(context : Context) = permissionList.all{
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}