package com.hdh.dev.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(ProductEntity::class, DepartmentEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getProductDao() : ProductDao
    abstract fun getDepartmentDao() : DepartmentDao

    companion object{
        val databaseName = "db_product"
        var appDatabase : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase?{
          /*  if(appDatabase == null){
                appDatabase = Room.databaseBuilder(
                                context,
                                AppDatabase::class.java,
                                databaseName
                            ).build()
            }*/
            if(appDatabase == null){
                synchronized(AppDatabase::class){
                    appDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        databaseName
                    ).addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            fillInDb(context.applicationContext)
                        }
                    }).build()
                }
            }

            return appDatabase
        }
        fun fillInDb(context: Context){
            CoroutineScope(Dispatchers.IO).launch {
                getInstance(context)!!.getProductDao().insertProductList(
                    PRODUCT_DATA
                )
            }
        }

    }

}


private val PRODUCT_DATA = arrayListOf(
    ProductEntity(null, "AAAAA", "덕다운 오버핏 양털 푸퍼 패딩", "https://www.prada.com/content/dam/pradabkg_products/2/292/292028/10PHF0627/292028_10PH_F0627_S_212_SLF.jpg/jcr:content/renditions/cq5dam.web.hebebed.800.800.jpg", "상의",510000,"B-2",5,0),
    ProductEntity(null, "BBBBB", "무스탕 자켓", "https://spao.com/web/product/small/202211/d9048b306e7dcd2745e7cc3354cebdf8.jpg", "상의",159000,"B-3",9,0),
    ProductEntity(null, "CCCCC", "테이퍼드 히든밴딩 슬랙스", "https://image.msscdn.net/images/goods_img/20220113/2305741/2305741_4_500.jpg?t=20220901152748", "하의",129800,"A-1",14,0)

)
