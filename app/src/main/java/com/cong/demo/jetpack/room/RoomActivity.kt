package com.cong.demo.jetpack.room

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityRoomBinding

/**
 * https://www.bilibili.com/video/BV1vk4y197kF
 * 
 * sqlite的封装
 * 
 */
class RoomActivity : BaseActivity(){
    
    private lateinit var binding:LayoutActivityRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        var dataBase = MyDataBase.getDataBase()
        var userDao = dataBase.userDao()
        
        var user = UserBean()
        user.id = 1
        user.age =100
        user.name = "ldx"
        userDao.insertData(user)
        
        
        LogUtils.i(userDao.queryAll().size)
        
    }
}