package com.hdh.dev.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("select * from ProductEntity")
    fun getAllProduct() : List<ProductEntity>

    @Insert
    fun insertProduct(product : ProductEntity)
}