package com.cong.demo.jetpack.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils

class CxiLifecycleObserver : LifecycleObserver{
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create(){
        LogUtils.i("====LifecycleObserver======create=====")
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start(){
        LogUtils.i("====LifecycleObserver======start=====")
    }
    
}