package com.cong.demo.jetpack.room

import androidx.room.*
import com.cong.demo.jetpack.User

@Dao
abstract  class UserDao {
    
    @Insert
    abstract fun insertData(user:UserBean)
    
    @Delete
    abstract fun deleteData(user:UserBean)
    
    //vararg 可变参数
    @Update
    abstract fun updateData(vararg user:UserBean)
    
    @Query("select * from t_user")
    abstract fun queryAll():List<UserBean>

    @Query("select * from t_user where id =:id")
    abstract fun queryUserByid(id:Long):UserBean

    @Query("select * from t_user where name =:name")
    abstract fun queryUserByid(name:String):List<UserBean>
}