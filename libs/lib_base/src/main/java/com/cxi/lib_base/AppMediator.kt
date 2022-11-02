package com.cxi.lib_base

import android.app.Application

class AppMediator private constructor(){
    
    companion object{
        fun getInstance() = Helper.instance
    }
    private object Helper{
        val instance = AppMediator()
    }

    interface AppInitial {
        fun initOnCreate(app: Application)
        fun initOnTerminate()
    }

    private val base = "com.cxi.lib_base.app.App"
    private val appClasses = arrayOf(
        base
    )
    
    fun initOnCreate(app: Application) {
        for (claName in appClasses) {
            try {
                val clz = Class.forName(claName)
                val obj = clz.newInstance() as AppInitial
                obj.initOnCreate(app)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun initOnTerminate() {
        for (claName in appClasses) {
            try {
                val clz = Class.forName(claName)
                val obj = clz.newInstance() as AppInitial
                obj.initOnTerminate()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }
    
}

