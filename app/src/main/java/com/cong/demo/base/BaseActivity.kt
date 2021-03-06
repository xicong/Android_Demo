package com.cong.demo.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils

open class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatus()
    }
    
    fun setStatus(){
        BarUtils.setStatusBarVisibility(this,true)
        BarUtils.setStatusBarColor(this,Color.WHITE)
        BarUtils.setStatusBarLightMode(this,true)
    }
    
    fun addTopViewMarginStatusHeight(view:View){
        BarUtils.addMarginTopEqualStatusBarHeight(view)
    }
    
    fun subTopViewMarginStatusHeight(view:View){
        BarUtils.subtractMarginTopEqualStatusBarHeight(view)
    }
    
}