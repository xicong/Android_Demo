package com.cong.demo.bluetooth

interface BluetoothScanCallback {

    fun onScan(result: MutableMap<String, HashMap<String, Any>>)
    
}