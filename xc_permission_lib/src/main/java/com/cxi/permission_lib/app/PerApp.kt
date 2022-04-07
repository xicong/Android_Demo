package com.cxi.permission_lib.app

import android.app.Application
import com.hjq.permissions.XXPermissions

object PerApp {

    var app:Application?=null
    
    fun permissionInit(application: Application,isScopedStorage:Boolean){
        app = application
        //当前项目是否已经适配了分区存储的特性
        XXPermissions.setScopedStorage(isScopedStorage);
    }
    
}