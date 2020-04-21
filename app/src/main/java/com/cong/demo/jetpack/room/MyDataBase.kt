package com.cong.demo.jetpack.room

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.cong.demo.app.CxiApp

/**
 * 创建一个数据库
 * 
 * entities  关联数据ben
 * 
 * version 数据库版本号
 */
@Database(entities = [UserBean::class],version = 1)
abstract  class MyDataBase : RoomDatabase() {
    
    //返回Dao的抽象方法
    abstract  fun userDao():UserDao
    
    //获取数据库
    companion object{
        private var database:MyDataBase? = null
        fun getDataBase():MyDataBase{
            if(database == null){
                database = Room.databaseBuilder(
                    CxiApp.instance,
                    MyDataBase::class.java,
                    "db_xc"  //数据库名称
                )
//                    .addCallback()  //监听
//                    .addMigrations(object : Migration(1,2){  //表示将数据库的版本号从1升级到2   注意不管升级或者降级都要把上面的同步改为新的版本号
//                        override fun migrate(database: SupportSQLiteDatabase) {
//                            database.execSQL("")
//                        }
//                    })  //升级时候的设置
                    .allowMainThreadQueries()   //允许在主线程操作数据库    平时不要用防止卡掉主线程
//                    .createFromAsset()  //不用默认的用外部数据库文件的时候设置
//                    .createFromFile()  //不用默认的用外部数据库文件的时候设置
                    .build()
            }
            return database as MyDataBase
        }
    }

}