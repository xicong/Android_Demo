package com.cong.demo.app
import android.app.Application

class CxiApp : Application(){
    
    //目前这种方式Context
    companion object {
        val instance: CxiApp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CxiApp() 
        }
    }
    
}