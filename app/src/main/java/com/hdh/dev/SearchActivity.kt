package com.hdh.dev

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.hdh.dev.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mSearch: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu);
        // Associate searchable configuration with the SearchView
        /*val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }*/
        mSearch=menu.findItem(R.id.search);
        val sv = mSearch.actionView as SearchView

        //확인버튼 활성화
        sv.isSubmitButtonEnabled = true
        menuInflater.inflate(R.menu.search_menu,menu);


        //SearchView의 검색 이벤트
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색버튼을 눌렀을 경우
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("gahee","검색버튼눌림")
                val text = binding.txtsearch
                text.text = query + "를 검색합니다."
                return true
            }

            //텍스트가 바뀔때마다 호출
            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("gahee","텍스트바뀜")
                val text = binding.txtresult
                text.text = "검색식 : $newText"
                return true
            }
        })
        return true
    }


}