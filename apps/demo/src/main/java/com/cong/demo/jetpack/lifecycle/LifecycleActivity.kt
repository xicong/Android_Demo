package com.cong.demo.jetpack.lifecycle

import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.jetpack.lifecycle.CxiLifecycleObserver

class LifecycleActivity : BaseActivity(){

//    private lateinit var binding:LayoutActivityLifecleDemoBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = LayoutActivityLifecleDemoBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        val observer = CxiLifecycleObserver()
        lifecycle.addObserver(observer)
    }
    
}