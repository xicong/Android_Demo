package com.cong.demo.app
import android.app.Application
import com.cxi.permission_lib.app.PerApp

class CxiApp : Application(){
    
    companion object {
        /**
         * lateinit 关键字
         *      lateinit只能修饰变量var，不能修饰常量val
         *      lateinit不能对可空类型使用
         *      lateinit不能对java基本类型使用，例如：Double、Int、Long等
         *      在调用lateinit修饰的变量时，如果变量还没有初始化，则会抛出未初始化异常，报错
         */
        lateinit var instance: CxiApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        PerApp.permissionInit(this,true)
    }
    
}