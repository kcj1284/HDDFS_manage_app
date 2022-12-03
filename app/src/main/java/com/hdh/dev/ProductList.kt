package com.hdh.dev

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hdh.dev.adapter.ViewpagerFragmentAdapter
import com.hdh.dev.databinding.ActivityAddProductBinding
import com.hdh.dev.databinding.ActivityMainBinding
import com.hdh.dev.databinding.ActivityProductListBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity

class ProductList : AppCompatActivity() , OnItemLongClickListener{

    private lateinit var binding : ActivityProductListBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        getProductList()
    }

    private fun getProductList(){
        Thread{
            val categoryList = listOf<String>("상의", "하의", "잡화")//ArrayList(productDao.getCategoryList())
            val productList = ArrayList<List<ProductEntity>>()
            categoryList.forEach {
                productList.add(productDao.getCategoryProduct(it))
            }
            Log.i("하하하", productList[0].toString())
            createFragmentAdapter(categoryList, productList)
        }.start()
    }

    private fun createFragmentAdapter(categoryList : List<String>, productList : List<List<ProductEntity>>){
        runOnUiThread {
            val viewPager : ViewPager2 = binding.viewPager
            val viewpagerFragmentAdapter = ViewpagerFragmentAdapter(this, productList)
            //list
            viewPager.adapter = viewpagerFragmentAdapter
            //탭
            TabLayoutMediator(binding.productlistTablayout, binding.viewPager, {tab, position -> tab.text = categoryList[position]}).attach()
        }
    }

    override fun onLongClick(productEntity : ProductEntity) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("상품삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("삭제", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                Thread{
                    productDao.deleteProduct(productEntity)
                    runOnUiThread {
                        Toast.makeText(this@ProductList, "삭제완료", Toast.LENGTH_SHORT).show()
                        onRestart()//이런식으로 가면 버전낮은 애들은 팅긴다는데... 추가 방안을 찾아보자
                    }
                }.start()
            }
        })
        builder.show()
    }

    override fun onRestart() {
        super.onRestart()
        getProductList()
    }
}