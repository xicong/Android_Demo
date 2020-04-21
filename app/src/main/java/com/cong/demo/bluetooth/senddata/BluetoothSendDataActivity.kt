package com.cong.demo.bluetooth.senddata

import android.os.Bundle
import com.cong.demo.base.BaseActivity
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
    }
    
}