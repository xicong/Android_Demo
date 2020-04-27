package com.cong.demo.bluetooth

/**
 * 打开蓝牙或者关闭蓝牙
 */
interface BluetoothOnOrOffCallBack {
    fun onOnOrOff(isOnOrOff:Boolean){}
}