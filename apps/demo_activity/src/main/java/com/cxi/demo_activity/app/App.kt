package com.cxi.demo_activity.app

import android.app.Application
import com.cxi.lib_base.AppMediator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppMediator.getInstance().initOnCreate(this)
    }
    
}