package com.hdh.dev

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.hdh.dev.databinding.ActivityQrSearchBinding
import com.google.common.util.concurrent.ListenableFuture
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrSearch : AppCompatActivity() {

    private lateinit var binding: ActivityQrSearchBinding
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao
    private lateinit var product : List<ProductEntity>

    private val PERMISSIONS_REQUEST_CODE = 1
    private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

    private var isDetected= false

    override fun onResume() {
        super.onResume()
        isDetected= false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQrSearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        if (!hasPermissions(this)) {
            // 카메라 권한을 요청합니다.
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            // 만약 이미 권한이 있다면 카메라를 시작합니다.
            startCamera()
        }
    }
    //권한 유무 확인
    fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Toast.makeText(this@QrSearch, "권한 요청이 승인되었습니다.", Toast.LENGTH_LONG).show()
                startCamera()
            } else {
                Toast.makeText(this@QrSearch, "권한 요청이 거부되었습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    // 미리보기와 이미지 분석을 시작합니다.
    fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = getPreview()
            val imageAnalysis = getImageAnalysis() // ❷
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.bindToLifecycle(this, cameraSelector,preview,imageAnalysis) // ❸

        }, ContextCompat.getMainExecutor(this))
    }

    //미리보기 객체를 반환합니다.
    fun getPreview(): Preview {
        val preview : Preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.barcodePreview.getSurfaceProvider())

        return preview
    }

    fun getImageAnalysis(): ImageAnalysis {

        val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        val imageAnalysis = ImageAnalysis.Builder().build()

        //Analyzer를 설정합니다.
        imageAnalysis.setAnalyzer(cameraExecutor, QRCodeAnalyzer(object : OnDetectListener {
            override fun onDetect(pcode: String) {
                if (!isDetected) {
                    isDetected = true // 데이터가 감지가 되었으므로 true로 바꾸어줍니다.
                    /*val intent = Intent(this@QrSearch, SearchResult::class.java)*/
                    Thread {
                        //결과(pcode)로 상품정보 불러오기
                        product = productDao.getDepartmentProductStock(
                            pcode,
                            StartActivity.DEPARTMENT_INDEX
                        ) as List<ProductEntity>

                        if(product.size>0){
                            //검색한 상품정보 상품상세(수정)탭에 넘기기
                            val intent = Intent(this@QrSearch, ProductEdit::class.java)
                            intent.putExtra("pid", product[0].pid.toString())
                            intent.putExtra("pcode", product[0].pcode)
                            intent.putExtra("image", product[0].image)
                            intent.putExtra("category", product[0].category)
                            intent.putExtra("name", product[0].pname)
                            intent.putExtra("price", product[0].price.toString())
                            intent.putExtra("location", product[0].loction)
                            intent.putExtra("stock", product[0].stock.toString())
                            intent.putExtra("did", product[0].did.toString())
                            //End
                            startActivity(intent)
                        }else{
                            Log.d("gahee","물품이없습니다")
                            runOnUiThread {
                                binding.errorText.text = "상품이 존재하지 않습니다."
                            }
                            isDetected = false
                            //Toast.makeText(this@QrSearch,"상품이 존재하지 않습니다.",Toast.LENGTH_LONG)
                        }

                    }.start()
                    // intent.putExtra("msg", msg)

                }
            }
        }))

        return imageAnalysis
    }

}