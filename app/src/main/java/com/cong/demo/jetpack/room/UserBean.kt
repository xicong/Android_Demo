package com.cong.demo.jetpack.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
设置标的名字  tableName = "t_user"   不加默认表的名字

 */
@Entity(tableName = "t_user")
class UserBean {
    
    //@PrimaryKey   主键   autoGenerate是否自动生成   true 是
    @PrimaryKey(autoGenerate = false)
    var id : Long = 0
    var name : String = ""
    var age:Int = 0
    
}