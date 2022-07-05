package com.cong.demo.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

class SystemBluetoothScanCallBack(private var bluetoothScanningResultCallback : BluetoothScanningResultCallback) :
    ScanCallback() {

    val scanner = mutableMapOf<String, BluetoothDeviceBean>()

    //当一个蓝牙ble广播被发现时回调
    override fun onScanResult(callbackType: Int, result: ScanResult?){
        super.onScanResult(callbackType, result)
        //扫描类型有开始扫描时传入的ScanSettings相关
        //对扫描到的设备进行操作。如：获取设备信息。
        if (result != null) {
            var map = HashMap<String, Any>()
            val device: BluetoothDevice = result.device
//            map["rssi"] = result.rssi
            scanner[device.address] = BluetoothDeviceBean(
                name = if (device.name.isNullOrEmpty()) "" else device.name,
                address = device.address,
                type = device.type,
                bondState = device.bondState
            )
            bluetoothScanningResultCallback.onScan(scanner)
        }
    }

    // 批量返回扫描结果。一般蓝牙设备对象都是通过onScanResult(int,ScanResult)返回，
    // 而不会在onBatchScanResults(List)方法中返回，除非手机支持批量扫描模式并且开启了批量扫描模式。
    // 批处理的开启请查看ScanSettings。
    //@param results 以前扫描到的扫描结果列表。
    override fun onBatchScanResults(results: List<ScanResult?>?) {
        super.onBatchScanResults(results)
    }

    //当扫描不能开启时回调
    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        //扫描太频繁会返回ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED，表示app无法注册，无法开始扫描。
    }
    
}