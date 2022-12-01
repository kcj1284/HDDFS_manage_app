package com.hdh.dev.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ProductEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getProductDao() : ProductDao

    companion object{
        val databaseName = "db_product"
        var appDatabase : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase?{
            if(appDatabase == null){
                appDatabase = Room.databaseBuilder(
                                context,
                                AppDatabase::class.java,
                                databaseName
                            ).build()
            }
            return appDatabase
        }
    }

}