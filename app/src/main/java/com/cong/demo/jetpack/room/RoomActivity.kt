package com.cong.demo.jetpack.room

import android.os.Bundle
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
    }
}