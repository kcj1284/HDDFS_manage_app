package com.hdh.dev.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Query("select * from ProductEntity")
    fun getAllProduct() : List<ProductEntity>
    //카테고리만 가져오기
    @Query("select category from ProductEntity group by category")
    fun getCategoryList() : List<String>
    //카테고리 이름대로 물건정보들 가져오기
    @Query("select * from ProductEntity where category = :cname and did = :departmentIndex")
    fun getCategoryProduct(cname : String, departmentIndex : Int) : List<ProductEntity>
    //검색
    @Query("select * from ProductEntity where pname like :searchQuery")
    fun searchProduct(searchQuery : String) : List<ProductEntity>

    @Insert
    fun insertProduct(product : ProductEntity)

    //수정
    @Update
    fun updateProduct(product : ProductEntity)

    @Delete
    fun deleteProduct(product : ProductEntity)
}