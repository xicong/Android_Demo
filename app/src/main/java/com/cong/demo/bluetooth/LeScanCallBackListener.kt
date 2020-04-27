package com.cong.demo.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.blankj.utilcode.util.LogUtils

/**
 * Le扫描结果回调
 */
class LeScanCallBackListener(private val bluetoothScanningResultCallback: BluetoothScanningResultCallback) : BluetoothAdapter.LeScanCallback {
    val scanner = mutableMapOf<String, BluetoothDeviceBean>()
    /**
     * @param device 扫描到的设备实例，可从实例中获取到相应的信息。如：名称，mac地址
     * @param rssi 可理解成设备的信号值。该数值是一个负数，越大则信号越强
     * @param scanRecord 远程设备提供的广播数据的内容
     */
    override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
        try {
//            map["rssi"] = rssi
            scanner[device!!.address] = BluetoothDeviceBean(
                name = device!!.name,
                address = device!!.address,
                type = device!!.type,
                bondState = device.bondState
            )
            bluetoothScanningResultCallback.onScan(scanner)
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }
}