package com.cong.demo.jetpack.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.R
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityNavigationBinding


/**
 * Navigation 默认切换用的replace 每次切换和返回都会重建
 * 稍耗费资源  并且有缓存影响
 * 
 * 替换默认的replace方式为show hide
 * 
 * 但是结合viewmodel其实数据缓存不会影响
 *
 * 
 * https://www.bilibili.com/video/BV1k7411f7TN
 */
class NavigationDemo : BaseActivity(){

    private lateinit var binding:LayoutActivityNavigationBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        //关联appbar  关联成功后appbar会显示返回键  文字显示fragment的label字段
        //我这里暂时在主题隐藏了appbar 所以不显示
        var controller : NavController = findNavController(R.id.fragment)
//        var appBarConfiguration = AppBarConfiguration(controller.graph)
//        setupActionBarWithNavController(controller,appBarConfiguration)
        
        //添加监听器 监听导航的目的地  传递的参数
//        controller.addOnDestinationChangedListener { controller, destination, arguments ->  
//            LogUtils.i(destination.toString())
//            LogUtils.i(arguments.toString())
//        }
        
        //Android 官方基于Md ui实现appbar   底部导航栏
        
        //演示关联toolbar
        binding.toolbar.setupWithNavController(controller)
        
        //实现一个showhide保证页面不销毁非方法
    }

    //这里实现appbar的返回按钮
    override fun onSupportNavigateUp(): Boolean {
        var controller : NavController = findNavController(R.id.fragment)
        return controller.navigateUp() || super.onSupportNavigateUp()
    }
    
    
}