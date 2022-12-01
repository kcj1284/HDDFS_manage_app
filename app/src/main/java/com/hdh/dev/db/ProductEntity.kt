package com.hdh.dev.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey val pid : String,
    @ColumnInfo(name = "pname") val pname : String,
    @ColumnInfo(name = "image") val image : String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "price") val price : Int,
    @ColumnInfo(name = "loction") var loction : String,//변경가능~
    @ColumnInfo(name = "stock") var stock : Int
)