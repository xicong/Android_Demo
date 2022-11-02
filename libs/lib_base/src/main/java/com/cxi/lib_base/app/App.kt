package com.cxi.lib_base.app

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.cxi.lib_base.AppMediator

class App : AppMediator.AppInitial {
    
    override fun initOnCreate(app: Application) {
        Utils.init(app)
    }

    override fun initOnTerminate() {
    }
    
}