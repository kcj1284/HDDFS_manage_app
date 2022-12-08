package com.hdh.dev.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hdh.dev.Announce


@Dao
interface AnnounceDao {

    @Query("SELECT * FROM announce")
    fun getAnnounceList():List<AnnounceEntity>


    @Insert
    fun insertAnnounceList(announce: List<AnnounceEntity>)

    @Insert
    fun insertAnnounce(announce: AnnounceEntity)

    @Delete
    fun deleteAnnounce(announce: AnnounceEntity)

}