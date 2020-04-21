package com.cong.demo.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils

/**
 * 广播获取扫描所有蓝色的结果
 */
class BluetoothScanBroadcastReceiver(private val bluetoothScanCallback:BluetoothScanCallback) : BroadcastReceiver() {
    
    private val scanner = mutableMapOf<String, HashMap<String, Any>>()

    fun dynamicRegistration() {
        scanner.clear()
        var intentFilter = IntentFilter()
        //获取搜索结果
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        //系统结束扫描  cancelDiscovery()都会触发ACTION_DISCOVERY_FINISHED
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        Utils.getApp().registerReceiver(this, intentFilter)
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent!!.action){
            BluetoothDevice.ACTION_FOUND ->{
                var device : BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                var map:HashMap<String,Any> = BluetoothHelper.parseDevice2Map(device)
                //获取蓝牙信号强度
                var  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,0)
                map["rssi"] = rssi
                scanner[map["address"].toString()] = map
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED->{
                ToastUtils.showLong("已开始扫描...")
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED->{
                ToastUtils.showLong("扫描完成")
                bluetoothScanCallback.onScan(scanner)
                Utils.getApp().unregisterReceiver(this)
            }
        }
        
    }
    
}