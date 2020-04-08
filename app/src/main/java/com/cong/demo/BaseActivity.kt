package com.cong.demo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils

open class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarVisibility(this,true)
//        BarUtils.setStatusBarColor(this,Color.WHITE)
    }
    
}