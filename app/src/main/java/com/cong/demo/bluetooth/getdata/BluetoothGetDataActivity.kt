package com.cong.demo.bluetooth.getdata

import android.os.Bundle
import com.blankj.utilcode.util.SpanUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.bluetooth.BluetoothHelper
import com.cong.demo.bluetooth.BluetoothScanCallback
import com.cong.demo.bluetooth.BluetoothOffCallBack
import com.cong.demo.bluetooth.BluetoothOnCallBack
import com.cong.demo.databinding.LayoutActivityBluetoothGetdataBinding

/**
 * https://www.jianshu.com/p/c7bb4e8f9fe6
 * https://www.jianshu.com/p/fb326a5709c8
 * 
 * 用于接收数据
 * 
 * 低功耗蓝牙设备版本要求
 * 当设备是目标连接设备时，Android版本最低为Android 4.3以上系统
 * 当设备是发起连接设备时，Android版本最低为Android 5.0以上系统
 * 
 * DEVICE_TYPE_CLASSIC  经典蓝牙      1
 * DEVICE_TYPE_LE            低功耗蓝牙    2
 * DEVICE_TYPE_DUAL       双向蓝牙     3
 * DEVICE_TYPE_UNKNOWN   未知类型  0
 */
class BluetoothGetDataActivity : BaseActivity() {
    
    private lateinit var mBinding:LayoutActivityBluetoothGetdataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityBluetoothGetdataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        
        mBinding.tvLayoutActivityGetdataSupportbluetooth.text = "设备是否支持蓝牙:${BluetoothHelper.isSupportBluetooth()}"
        updateBluetoothStatus()
        mBinding.tvLayoutActivityGetdataStatus.setOnClickListener {
            if(BluetoothHelper.isBluetoothEnabled()){
                BluetoothHelper.disableBluetooth(object :BluetoothOffCallBack{
                    override fun onOff() {
                        super.onOff()
                        updateBluetoothStatus()
                    }
                })
            }else{
                BluetoothHelper.enabledBluetooth(object : BluetoothOnCallBack{
                    override fun onOn() {
                        super.onOn()
                        updateBluetoothStatus()
                    }
                })
            }
        }
        
        //搜索连接过的所有设备
        var listlgd = BluetoothHelper.getBondedDevice()
        var spanUtilssgd =SpanUtils()
        listlgd.forEach {
            spanUtilssgd
                .append("\n")
                .setLeadingMargin(0,2)
                .append(it.value["name"].toString()+" = "+it.key+" = "+ it.value["type"].toString())
        }
        mBinding.tvLayoutActivityGetdataBluetoothLgd.text = "连接过的设备:${spanUtilssgd.create()}"

        
        
        //所有能搜到的普通蓝牙和低功耗
        BluetoothHelper.findLEAndClassic(6,object : BluetoothScanCallback{
            override fun onScan(result: MutableMap<String, HashMap<String, Any>>) {
                var spanUtilspthdgh =SpanUtils()
                result.forEach { 
                    spanUtilspthdgh
                        .append("\n")
                        .setLeadingMargin(0,2)
                        .append(it.value["name"].toString()+" = "+it.key+" = "+ it.value["type"].toString())
                }
                mBinding.tvLayoutActivityGetdataBluetoothSyd.text = "搜到的普通的和低功耗的: ${spanUtilspthdgh.create()}"
            }
        })
       

        //搜索低功耗蓝牙
        BluetoothHelper.findLE(6,object : BluetoothScanCallback{
            override fun onScan(result: MutableMap<String, HashMap<String, Any>>) {
                var spanUtilsdgh =SpanUtils()
                result.forEach {
                    spanUtilsdgh
                        .append("\n")
                        .setLeadingMargin(0,2)
                        .append(it.value["name"].toString()+" = "+it.key+" = "+ it.value["type"].toString())
                }
                mBinding.tvLayoutActivityGetdataBluetoothDgh.text = "低功耗的蓝牙设备: ${spanUtilsdgh.create()}"
            }
        })
    }
    
    
    fun updateBluetoothStatus(){
        mBinding.tvLayoutActivityGetdataStatus.text =  "蓝牙是否已打开:${BluetoothHelper.isBluetoothEnabled()},若关闭状态，可单击打开"
    }
    
    
}