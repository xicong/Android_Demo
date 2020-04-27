package com.cong.demo.bluetooth

interface BluetoothScanningResultCallback {

    fun onScan(result: MutableMap<String, BluetoothDeviceBean>)
    
}