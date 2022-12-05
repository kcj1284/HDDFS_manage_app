package com.hdh.dev.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DepartmentEntity(
    @PrimaryKey val did : Int,
    @ColumnInfo(name = "dname") val dname : String
)