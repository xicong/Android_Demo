package com.cong.demo.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils

/**
 * 蓝牙关闭
 */
class BluetoothOffBroadcastReceiver(private var bluetoothOffCallBack: BluetoothOffCallBack) :
    BroadcastReceiver() {
    
    fun dynamicRegistration() {
        var intentFilter = IntentFilter()
        //监视蓝牙打开和关闭的状态
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        Utils.getApp().registerReceiver(this, intentFilter)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent!!.action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val bluetoothStatus = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                when (bluetoothStatus) {
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        ToastUtils.showLong("蓝牙正在关闭中...")
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        ToastUtils.showLong("蓝牙已关闭")
                        bluetoothOffCallBack?.onOff()
                    }
                }
            }
        }
        Utils.getApp().unregisterReceiver(this)
    }

}