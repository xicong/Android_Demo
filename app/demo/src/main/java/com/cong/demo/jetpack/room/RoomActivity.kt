package com.cong.demo.jetpack.room

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityRoomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * https://www.bilibili.com/video/BV1vk4y197kF
 * 
 * sqlite的封装
 * 
 */
class RoomActivity : BaseActivity(){
    
    private lateinit var binding:LayoutActivityRoomBinding
    
    var userDao: UserDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        var dataBase = MyDataBase.getDataBase()
        userDao = dataBase.userDao()


        GlobalScope.launch(Dispatchers.IO){
//            cr()
//            cr()
            gx()
            LogUtils.i(userDao!!.queryAll().size)
        }
    }

    fun gx(){
        var user = UserBean()
        user.id = 1
        user.age =100
        user.name = "ldx"
        LogUtils.i(userDao!!.updateData(user))
    }
    
    fun cr(){
        var user = UserBean()
        user.id = 1
        user.age =100
        user.name = "ldx"
        userDao!!.insertData(user)
    }
}