package com.cxi.permission_lib

import android.Manifest
import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

object XcPermissionUtils{
    
    private fun goSetPer(activity: Activity,permissions: MutableList<String>?){
        MaterialDialog(activity).show {
            title(text = "提示!")
            message(text = "您已拒绝我们申请授权，请去设置里面开启，便可完美体验所有功能")
            cancelOnTouchOutside(false)
            cancelable(false)
            positiveButton(text = "去设置"){
                XXPermissions.startPermissionActivity(activity, permissions);
            }
            negativeButton(text = "取消")
        }
    }

    //申请定位权限
    fun locationPermission(activity:Activity,ok:()->Unit,no:((errMsg:String)->Unit)?= null){
        XXPermissions
            .with(activity)
            .permission(Permission.ACCESS_FINE_LOCATION,Permission.ACCESS_COARSE_LOCATION,Permission.ACCESS_BACKGROUND_LOCATION)
            .request(object:OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all){
                        ok.invoke()
                    }else{
                        no?.invoke("获取到部分定位相关权限")
                    }
                }
                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never){
                        no?.invoke("您未授权定位相关所有权限")
                        goSetPer(activity,permissions)
                    }else{
                        no?.invoke("获取定位权限失败")
                    }
                }
            })
    }

    //判断并申请蓝牙的权限
    fun blePermission(activity:Activity,ok:()->Unit,no:((errMsg:String)->Unit)?= null){
        locationPermission(activity,
            ok = {
                ok.invoke()
            },
            no = {
                no?.invoke(it)
            }
        )
    }


    //申请文件读写权限
    fun fileWritePermission(activity:Activity,ok:()->Unit,no:((errMsg:String)->Unit) ?= null){
        XXPermissions
            .with(activity)
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object:OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all){
                        ok.invoke()
                    }else{
                        no?.invoke("获取到部分文件读写相关权限")
                    }
                }
                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never){
                        no?.invoke("您未授权文件存储相关所有权限")
                        goSetPer(activity,permissions)
                    }else{
                        no?.invoke("获取文件读写权限失败")
                    }
                }
            })
    }
    
}



