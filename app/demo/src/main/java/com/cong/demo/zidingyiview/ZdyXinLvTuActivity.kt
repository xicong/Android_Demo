package com.cong.demo.zidingyiview

import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityXinlvtuBinding

/**
 * 实现一个能实时更新的心率动态图
 */
class ZdyXinLvTuActivity  : BaseActivity() {
    
    private lateinit var mBinding:LayoutActivityXinlvtuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityXinlvtuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
    
}