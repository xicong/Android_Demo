package com.cong.demo.bluetooth.getdata

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.bluetooth.*
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
 * 
 * 
 *  BOND_NONE = 10;     远程设备未绑定
 *  BOND_BONDING = 11;    正在与远程设备绑定
 *  BOND_BONDED = 12;    表示远程设备已绑定
 */
class BluetoothGetDataActivity : BaseActivity() {

    private lateinit var mBinding: LayoutActivityBluetoothGetdataBinding
    private var  mBluetoothGatt:BluetoothGatt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityBluetoothGetdataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        BluetoothHelper.isSupportBluetooth(object : BluetoothIsSupportCallBack {
            override fun isSupport(isSupport: Boolean) {
                super.isSupport(isSupport)
                mBinding.tvLayoutActivityGetdataSupportbluetooth.text =
                    "设备是否支持蓝牙:${isSupport}"
            }
        })

        updateBluetoothStatus()
        mBinding.tvLayoutActivityGetdataStatus.setOnClickListener {
            BluetoothHelper.isBluetoothEnabled(object : BluetoothOnOrOffCallBack {
                override fun onOnOrOff(isOnOrOff: Boolean) {
                    super.onOnOrOff(isOnOrOff)
                    if (isOnOrOff) {
                        BluetoothHelper.disableBluetooth(object : BluetoothOnOrOffCallBack {
                            override fun onOnOrOff(isOnOrOff: Boolean) {
                                super.onOnOrOff(isOnOrOff)
                                updateBluetoothStatus()
                            }
                        })
                    } else {
                        BluetoothHelper.enabledBluetooth(object : BluetoothOnOrOffCallBack {
                            override fun onOnOrOff(isOnOrOff: Boolean) {
                                super.onOnOrOff(isOnOrOff)
                                updateBluetoothStatus()
                            }
                        })
                    }
                }
            })
        }

        //搜索连接过的所有设备
        searchLjgde()

        //所有能搜到的普通蓝牙和低功耗
        searchBluetooth()

        //搜索低功耗蓝牙
        searchBluetoothLE()

    }
    
    private fun islianjie(address:String){
        BluetoothHelper.isConnectedDevice(address,object:IsConnectedDeviceSuccessCallBack{
            override fun isConnectedSuccess(isSuccess: Boolean) {
                super.isConnectedSuccess(isSuccess)
                if(isSuccess){
                    ToastUtils.showLong("已经是连接状态了")
                }else{
                    lianjie(address)
                }
            }
        })
    }

    private fun searchLjgde() {
        BluetoothHelper.getBondedDevice(object : BluetoothScanningResultCallback {
            override fun onScan(result: MutableMap<String, BluetoothDeviceBean>) {
                var spanUtilssgd = SpanUtils
                    .with(mBinding.tvLayoutActivityGetdataBluetoothLgd)
                    .appendLine()
                    .appendLine("已连接过的设备")
                    .appendLine()
                    .appendLine("重新获取").setClickSpan(object: ClickableSpan(){
                        override fun onClick(widget: View) {
                            ToastUtils.showLong("重新获取")
                            searchLjgde()
                        }
                        override fun updateDrawState(ds: TextPaint) {
//                           super.updateDrawState(ds)
                            ds.color = Color.BLUE
                            ds.isUnderlineText = false
                        }
                    })
                    .appendLine()
                result.forEach {
                    spanUtilssgd
                        .appendLine(
                            "蓝牙名称："+it.value.name + "······地址：" + it.value.address + "······蓝牙类型：" + it.value.type+"······绑定状态："+it.value.bondState
                        )
                        .setClickSpan(object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                islianjie(it.value.address)
                            }
                            override fun updateDrawState(ds: TextPaint) {
//                           super.updateDrawState(ds)
                                ds.color = Color.BLUE
                                ds.isUnderlineText = false
                            }
                        })
                    xianshilianjiezhuangtai(it.value.address,spanUtilssgd) 
                }
                spanUtilssgd.create()
                ToastUtils.showLong("获取完成")
            }
        })
    }
    
    private  fun  xianshilianjiezhuangtai(addrsss:String,spanUtils: SpanUtils){
        BluetoothHelper.isConnectedDevice(
            addrsss
            ,object :IsConnectedDeviceSuccessCallBack{
                override fun isConnectedSuccess(isSuccess: Boolean) {
                    super.isConnectedSuccess(isSuccess)
                    spanUtils
                        .append("连接状态${isSuccess}")
                        .appendLine()
                }
            })
    }


    private fun searchBluetoothLE() {
        BluetoothHelper.findLE(6, object : BluetoothScanningResultCallback {
            override fun onScan(result: MutableMap<String, BluetoothDeviceBean>) {
                var span = SpanUtils.with(mBinding.tvLayoutActivityGetdataBluetoothDgh)
                    .appendLine("低功耗的蓝牙设备: ")
                    .appendLine()
                    .appendLine("重新扫描").setClickSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            searchBluetoothLE()
                        }
                        override fun updateDrawState(ds: TextPaint) {
//                          super.updateDrawState(ds)
                            ds.color = Color.BLUE
                            ds.isUnderlineText = false
                        }
                    }).appendLine()
                    .appendLine("搜索结果")
                    .appendLine()
                result.forEach {
                    span
                        .appendLine(
                            "蓝牙名称："+it.value.name + "······地址：" + it.value.address + "······蓝牙类型：" + it.value.type+"······绑定状态："+it.value.bondState
                        )
                        .setClickSpan(object :
                            ClickableSpan() {
                            override fun onClick(widget: View) {
                                islianjie(it.value.address)
                            }

                            override fun updateDrawState(ds: TextPaint) {
//                                super.updateDrawState(ds)
                                ds.color = Color.BLUE
                                ds.isUnderlineText = false
                            }
                        })
                    xianshilianjiezhuangtai(it.value.address,span)
                }
                span.create()
            }
        })
    }
    
    private fun lianjie(address:String) {
        DeviceControlActivity.go(address)
    }


    private fun searchBluetooth() {
        BluetoothHelper.findLEAndClassic(6, object : BluetoothScanningResultCallback {
            override fun onScan(result: MutableMap<String, BluetoothDeviceBean>) {
                var spanptd = SpanUtils.with(mBinding.tvLayoutActivityGetdataBluetoothSyd)
                    .appendLine("搜到的普通的和低功耗的: ")
                    .appendLine()
                    .appendLine("重新扫描").setClickSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            searchBluetooth()
                        }

                        override fun updateDrawState(ds: TextPaint) {
//                           super.updateDrawState(ds)
                            ds.color = Color.BLUE
                            ds.isUnderlineText = false
                        }
                    }).appendLine()
                    .appendLine("搜索结果")
                    .appendLine()
                result.forEach {
                    spanptd
                        .appendLine(
                            "蓝牙名称："+it.value.name + "······地址：" + it.value.address + "······蓝牙类型：" + it.value.type+"······绑定状态："+it.value.bondState
                        )
                        .setClickSpan(object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                islianjie(it.value.address)
                            }

                            override fun updateDrawState(ds: TextPaint) {
//                                super.updateDrawState(ds)
                                ds.color = Color.BLUE
                                ds.isUnderlineText = false
                            }
                        })
                    xianshilianjiezhuangtai(it.value.address,spanptd)
                }
                spanptd.create()
            }
        })
    }

    private fun updateBluetoothStatus() {
        BluetoothHelper.isBluetoothEnabled(object : BluetoothOnOrOffCallBack {
            override fun onOnOrOff(isOnOrOff: Boolean) {
                super.onOnOrOff(isOnOrOff)
                mBinding.tvLayoutActivityGetdataStatus.text =
                    "蓝牙是否已打开:${isOnOrOff},若关闭状态，可单击打开"
            }
        })

    }


}