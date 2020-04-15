package com.cong.demo.app

import android.app.Application

class CxiApp : Application() {

    companion object {
        val instance: CxiApp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CxiApp() 
        }
    }
    
}