package com.cxi.permission_lib.app

import android.app.Application
import com.hjq.permissions.XXPermissions

object PerApp {

    var app:Application?=null
    
    fun permissionInit(application: Application,isScopedStorage:Boolean){
        app = application
    }
    
}