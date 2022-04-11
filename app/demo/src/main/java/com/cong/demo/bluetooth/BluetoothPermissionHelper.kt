package com.cong.demo.bluetooth

import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils

object BluetoothPermissionHelper {

    /**
     * 判断蓝牙权限
     * true 有权限  false 没有权限
     */
    fun isBluetoothPermissionAndRequest(bluetoothPermissionRequestCallBack: BluetoothPermissionRequestCallBack) {
        if (!PermissionUtils.isGranted(
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            )
        ) {
            ToastUtils.showLong("需要先给予蓝牙相关权限")
            if (!PermissionUtils.isGranted(
                    "android.permission.BLUETOOTH",
                    "android.permission.BLUETOOTH_ADMIN",
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_FINE_LOCATION"
                )
            ) {
                PermissionUtils
                    .permission(PermissionConstants.LOCATION)
                    .rationale { activity, shouldRequest ->
                        MaterialDialog(Utils.getApp())
                            .title(text = "提示")
                            .message(text = "您已拒绝我们申请授权，请同意授权，否则该功能将无法正常使用！")
                            .positiveButton(text = "确定")
                            .negativeButton(text = "去设置") {
                                PermissionUtils.launchAppDetailsSettings()
                            }
                            .show()
                    }
                    .callback(object : PermissionUtils.FullCallback {
                        override fun onGranted(permissionsGranted: List<String>) {
                            bluetoothPermissionRequestCallBack.onGranted()
                        }
                        override fun onDenied(
                            permissionsDeniedForever: List<String>,
                            permissionsDenied: List<String>
                        ) {
                            LogUtils.d(permissionsDeniedForever, permissionsDenied)
                            ToastUtils.showLong("拒绝该权限后将无法使用该功能")
                        }
                    })
                    .request()
            } else {
                bluetoothPermissionRequestCallBack.onGranted()
            }
        }else{
            bluetoothPermissionRequestCallBack.onGranted()
        }
    }
}