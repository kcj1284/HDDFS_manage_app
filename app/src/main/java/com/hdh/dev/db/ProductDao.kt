package com.hdh.dev.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("select * from ProductEntity")
    fun getAllProduct() : List<ProductEntity>
    //카테고리만 가져오기
    @Query("select category from ProductEntity group by category")
    fun getCategoryList() : List<String>
    //카테고리 이름대로 물건정보들 가져오기
    @Query("select * from ProductEntity where category = :cname")
    fun getCategoryProduct(cname : String) : List<ProductEntity>

    @Insert
    fun insertProduct(product : ProductEntity)
}