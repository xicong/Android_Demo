package com.cong.demo.activity

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.R

class Activity_ShengMingZhouQi  : BaseActivity(){

    /**
     * 当我们点击activity的时候，系统会调用activity的oncreate()方法，在这个方法中我们会初始化当前布局setContentLayout（）方法。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_actiivty_shengmingzhouqi)
        LogUtils.i("=============onCreate=================")
    }

    /**
     * onCreate()方法完成后，此时activity进入onStart()方法,当前activity是用户可见状态，但没有焦点，与用户不能交互，一般可在当前方法做一些动画的初始化操作
     */
    override fun onStart() {
        super.onStart()
        LogUtils.i("=============onStart=================")
    }

    /**
     * onStart()方法完成之后，此时activity进入onResume()方法中，当前activity状态属于运行状态 (Running)，可与用户进行交互。
     */
    override fun onResume() {
        super.onResume()
        LogUtils.i("=============onResume=================")
    }

    /**
     * 当另外一个activity覆盖当前的acitivty时，此时当前activity会进入到onPouse()方法中，当前activity是可见的，但不能与用户交互状态。
     */
    override fun onPause() {
        super.onPause()
        LogUtils.i("=============onPause=================")
    }


    /**
     * onPouse()方法完成之后，此时activity进入onStop()方法，此时activity对用户是不可见的，在系统内存紧张的情况下，有可能会被系统进行回收。所以一般在当前方法可做资源回收。
     */
    override fun onStop() {
        super.onStop()
        LogUtils.i("=============onStop=================")
    }
    
    /**
     * onStop()方法完成之后，此时activity进入到onDestory()方法中，结束当前activity。
     */
    override fun onDestroy() {
        super.onDestroy()
        LogUtils.i("=============onDestroy=================")
    }

    /**
     * onRestart()方法在用户按下home()之后，再次进入到当前activity的时候调用。调用顺序onPouse()->onStop()->onRestart()->onStart()->onResume().
     */
    override fun onRestart() {
        super.onRestart()
        LogUtils.i("=============onRestart=================")
    }
    
}