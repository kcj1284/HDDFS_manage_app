package com.hdh.dev

import android.content.AbstractThreadedSyncAdapter
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.hdh.dev.adapter.PlistFragmentRecyclerViewAdapter
import com.hdh.dev.adapter.ViewpagerFragmentAdapter
import com.hdh.dev.databinding.ActivityAddProductBinding
import com.hdh.dev.databinding.ActivityMainBinding
import com.hdh.dev.databinding.ActivityProductListBinding
import com.hdh.dev.db.AppDatabase
import com.hdh.dev.db.ProductDao
import com.hdh.dev.db.ProductEntity

class ProductList : AppCompatActivity() , OnItemLongClickListener, NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding : ActivityProductListBinding
    private lateinit var db : AppDatabase
    private lateinit var productDao : ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        productDao = db.getProductDao()

        //툴바설정
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setDisplayShowTitleEnabled(true)    // 타이틀 보이게 하기
        binding.navigationView.setNavigationItemSelectedListener(this)

        val departmentList = arrayOf("무역센터점", "목동점", "천호점","관리자모드")

        //네비게이션 헤더에 지점명 출력
        val headerTxt = binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.branch)
        headerTxt.setText(departmentList[StartActivity.DEPARTMENT_INDEX])
        //네비게이션 헤더에 지점사진 출력
        val headerImg = binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_image)
        when(StartActivity.DEPARTMENT_INDEX) {
            1 -> headerImg.setImageDrawable(getResources().getDrawable(R.drawable.mokdong))
            2 -> headerImg.setImageDrawable(getResources().getDrawable(R.drawable.cheonho))
            3 -> headerImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round))
        }
        getProductList()
    }

    private fun getProductList(){
        Thread{
            val categoryList = listOf<String>("상의", "하의", "잡화", "품절임박", "품절제품")//ArrayList(productDao.getCategoryList())
            val productList = ArrayList<ArrayList<ProductEntity>>()

            //상의,하의,잡화 카테고리들가져오기
            for(i in 0..2){
                productList.add(productDao.getCategoryProduct(categoryList[i], StartActivity.DEPARTMENT_INDEX) as ArrayList<ProductEntity>)
            }
            //품절임박제품들 가져오기
            productList.add(productDao.getProductStockDown(StartActivity.DEPARTMENT_INDEX) as ArrayList<ProductEntity>)
            //품절제품들 가져오기
            productList.add(productDao.getProductStockZero(StartActivity.DEPARTMENT_INDEX) as ArrayList<ProductEntity>)

            //총 5개의 물건 ArrayList가 들어감
            createFragmentAdapter(categoryList, productList)
        }.start()
    }

    private fun createFragmentAdapter(categoryList : List<String>, productList : List<ArrayList<ProductEntity>>){
        runOnUiThread {
            val viewPager : ViewPager2 = binding.viewPager
            val viewpagerFragmentAdapter = ViewpagerFragmentAdapter(this, productList)
            //list
            viewPager.adapter = viewpagerFragmentAdapter
            //탭
            TabLayoutMediator(binding.productlistTablayout, binding.viewPager, {tab, position -> tab.text = categoryList[position]}).attach()
        }
    }

    override fun onLongClick(position : Int, adapter: PlistFragmentRecyclerViewAdapter) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("상품삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("삭제", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                Thread{
                    productDao.deleteProduct(adapter.productList[position])//db에서 먼저 제품 지우고
                    adapter.productList.removeAt(position)//adapter에 서 가지고있는 list에서도 빼주기
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@ProductList, "삭제완료", Toast.LENGTH_SHORT).show()
                        //onRestart()//이런식으로 가면 버전낮은 애들은 팅긴다는데... 추가 방안을 찾아보자
                    }
                }.start()
            }
        })
        builder.show()
    }

    // 물건 수정하고 돌아왔을때는 activity 다시 띄우자
    override fun onRestart() {
        super.onRestart()
        getProductList()
    }

    //메뉴 아이콘 누르면 네비게이션바열리기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //뒤로가기 했을 때 네비게이션바닫히기
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //네비게이션바에서 메뉴이동하기
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("gahee","버튼이눌렷다아아아아 $item")
        when(item.itemId){
            R.id.home_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentHome = Intent(this, MainActivity::class.java)
                startActivity(intentHome)
            }
            R.id.add_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentAddProduct = Intent(this, AddProduct::class.java)
                startActivity(intentAddProduct)
            }
            R.id.search_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentSearch = Intent(this, SearchActivity::class.java)
                startActivity(intentSearch)
            }
            R.id.qrSearch_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentQrSearch = Intent(this, QrSearch::class.java)
                startActivity(intentQrSearch)
            }
            R.id.stock_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentStock = Intent(this, ProductList::class.java)
                startActivity(intentStock)
            }
            R.id.setting_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentSetting = Intent(this, SetApp::class.java)
                startActivity(intentSetting)
            }
            R.id.announcement_item_menu_btn->{
                Log.d("gahee","버튼눌림")
                val intentAnnounce = Intent(this, Announce::class.java)
                startActivity(intentAnnounce)
            }
        }
        binding.drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }
}