package com.cong.demo.bluetooth.senddata

import android.annotation.SuppressLint
import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.bluetooth.BluetoothHelper
import com.cong.demo.bluetooth.BluetoothIsSupportCallBack
import com.cong.demo.bluetooth.BluetoothOnOrOffCallBack
import com.cong.demo.databinding.LayoutActivityBluetoothSenddataBinding

/**
 * 用于智能硬件模拟发送数据
 */
class BluetoothSendDataActivity : BaseActivity() {
    
    private lateinit var mBinding:LayoutActivityBluetoothSenddataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityBluetoothSenddataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        
        //显示常规的是否支持蓝牙
        //蓝牙是否打开
        //蓝牙支持手动点击打开关闭蓝牙，并实现监听
        //显示蓝牙的mac地址
        BluetoothHelper.isSupportBluetooth(object : BluetoothIsSupportCallBack{
            @SuppressLint("SetTextI18n")
            override fun isSupport(isSupport: Boolean) {
                super.isSupport(isSupport)
                mBinding.tvActivitySenddataIssupperbluetooth.text = "是否支持蓝牙: ${isSupport}"
            }
        })
       
        
        bluetoothStatus()
        mBinding.tvActivitySenddataOpenorcloseBluetooth.setOnClickListener {
            BluetoothHelper.isBluetoothEnabled(object : BluetoothOnOrOffCallBack{
                override fun onOnOrOff(isOnOrOff: Boolean) {
                    super.onOnOrOff(isOnOrOff)
                    if (isOnOrOff){
                        BluetoothHelper.disableBluetooth(object : BluetoothOnOrOffCallBack{
                            override fun onOnOrOff(isOnOrOff: Boolean) {
                                super.onOnOrOff(isOnOrOff)
                                bluetoothStatus()
                            }
                        })
                    }else{
                        BluetoothHelper.enabledBluetooth(object : BluetoothOnOrOffCallBack{
                            override fun onOnOrOff(isOnOrOff: Boolean) {
                                super.onOnOrOff(isOnOrOff)
                                bluetoothStatus()
                            }
                        })
                    }
                }
            })
        }
        
        //02:00:00:00:00:00
        //E4:47:90:3C:5B:34
        mBinding.tvActivitySenddataBluetoothAddrss.text = "本地蓝牙适配器的硬件地址是: ${BluetoothHelper.getBAdapter().address}"
        
    }
    
    fun bluetoothStatus(){
        BluetoothHelper.isBluetoothEnabled(object :BluetoothOnOrOffCallBack{
            override fun onOnOrOff(isOnOrOff: Boolean) {
                super.onOnOrOff(isOnOrOff)
                mBinding.tvActivitySenddataOpenorcloseBluetooth.text = "蓝牙是否打开: ${isOnOrOff}"
            }
        })
    }
    
}