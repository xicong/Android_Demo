package com.cong.demo.jetpack.room

import androidx.room.*
import com.cong.demo.jetpack.User

@Dao
abstract  class UserDao {
    
    @Insert
    abstract fun insertData(user:User)
    
    @Delete
    abstract fun deleteData(user:User)
    
    //vararg 可变参数
    @Update
    abstract fun updateData(vararg user:User)
    
    @Query("select * from t_user")
    abstract fun queryAll():List<User>

    @Query("select * from t_user where id =:id")
    abstract fun queryUserByid(id:Long):User

    @Query("select * from t_user where name =:name")
    abstract fun queryUserByid(name:String):List<User>
}