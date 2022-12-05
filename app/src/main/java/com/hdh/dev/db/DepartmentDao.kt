package com.hdh.dev.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepartmentDao {
    //지점최초생성전 엔티티가 있는지 확인하기위한 용도
    @Query("select did from DepartmentEntity")
    fun getDepartment() : List<Int>
    //지점 넣기
    @Insert
    fun insertDepartment(de : DepartmentEntity)
}