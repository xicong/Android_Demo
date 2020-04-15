package com.cong.demo.jetpack.livedata

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityLivedataBinding

/**
 * https://www.bilibili.com/video/BV1uE411n7WY
 * 
 * LIveData  内部实现了观察activity生命周期的能力
 *                  采用观察着模式
 *                  能够保证数据和ui统一
 *                  减少内存泄露
 *                  当Activity停止时不会引起崩溃
 *                  不需要额外的手动处理来影响生命周期的变化
 *                  组件和数据的相关内容能实时更新
 *                  资源共享
 *                  针对configuration时，不需要额外的处理来保存数据
 *                  
 *  LiveData与MutableLiveData的其实在概念上是一模一样的.唯一几个的区别如下:
 *              1.MutableLiveData的父类是LiveData
 *              2.LiveData在实体类里可以通知指定某个字段的数据更新.
 *              3.MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
 */
class LIveDataActivity : BaseActivity(){

    private lateinit var binding: LayoutActivityLivedataBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityLivedataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mViewModel = ViewModelProvider(this).get(LiveDataViewModel::class.java)
        mViewModel.livedata.observe(this, Observer {
            binding.tvActivityLivedateNum.text = it.toString()
        } )
        binding.tvActivityLivedateStart.setOnClickListener {
            mViewModel.start()
        }
        binding.tvActivityLivedateStop.setOnClickListener { 
            mViewModel.stop()
        }
    }
    
}