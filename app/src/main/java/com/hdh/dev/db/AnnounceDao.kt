package com.hdh.dev.db

import androidx.room.*
import com.hdh.dev.Announce


@Dao
interface AnnounceDao {

    @Query("SELECT * FROM announce")
    fun getAnnounceList():List<AnnounceEntity>

    @Insert
    fun insertAnnounceList(announce: List<AnnounceEntity>)

    @Insert
    fun insertAnnounce(announce: AnnounceEntity)

    //수정
    @Update
    fun updateAnnounce(announce: AnnounceEntity)

    @Delete
    fun deleteAnnounce(announce: AnnounceEntity)

}