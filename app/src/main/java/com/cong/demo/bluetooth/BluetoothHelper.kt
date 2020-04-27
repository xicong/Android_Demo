package com.cong.demo.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.*
import kotlinx.coroutines.*


object BluetoothHelper {

    /**
     * 打开蓝牙
     */
    @SuppressLint("MissingPermission")
    fun enabledBluetooth(bluetoothOnOrOffCallBack: BluetoothOnOrOffCallBack) {
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    val bluetoothOnBroadcastReceiver =
                        BluetoothOnBroadcastReceiverListener(object : BluetoothOnOrOffCallBack {
                            override fun onOnOrOff(isOnOrOff: Boolean) {
                                super.onOnOrOff(isOnOrOff)
                                bluetoothOnOrOffCallBack.onOnOrOff(isOnOrOff)
                            }
                        })
                    bluetoothOnBroadcastReceiver.dynamicRegistration()
                    getBAdapter()!!.enable()
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    /**
     * 关闭蓝牙
     * 方式一：请求打开蓝牙
     * ntent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
     * activity.startActivityForResult(intent, 1);
     * 方式二：半静默打开蓝牙
     * 低版本android会静默打开蓝牙，高版本android会请求打开蓝牙
     * getBAdapter()!!.disable()
     */
    @SuppressLint("MissingPermission")
    fun disableBluetooth(bluetoothOnOrOffCallBack: BluetoothOnOrOffCallBack) {
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    val bluetoothOffBroadcastReceiver =
                        BluetoothOffBroadcastReceiverListener(object : BluetoothOnOrOffCallBack {
                            override fun onOnOrOff(isOnOrOff: Boolean) {
                                super.onOnOrOff(isOnOrOff)
                                bluetoothOnOrOffCallBack.onOnOrOff(isOnOrOff)
                            }
                        })
                    bluetoothOffBroadcastReceiver.dynamicRegistration()
                    getBAdapter()!!.disable()
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    /**
     * 判断设备是否支持蓝牙
     */
    fun isSupportBluetooth(bluetoothIsSupportCallBack: BluetoothIsSupportCallBack) {
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    bluetoothIsSupportCallBack.isSupport(getBAdapter() != null)
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    /**
     * 判断蓝牙是否已打开
     *
     * @return  true 打开  false 未打开
     */
    @SuppressLint("MissingPermission")
    fun isBluetoothEnabled(bluetoothOnOrOffCallBack: BluetoothOnOrOffCallBack) {
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    bluetoothOnOrOffCallBack.onOnOrOff(getBAdapter().isEnabled)
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }


    /**
     * 查询已配对的蓝牙设备
     */
    @SuppressLint("MissingPermission")
    fun getBondedDevice(bluetoothScanningResultCallback: BluetoothScanningResultCallback) {
        val scanner = mutableMapOf<String, BluetoothDeviceBean>()
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    val adapter: BluetoothAdapter = getBAdapter()
                    val pairedDevices: Set<BluetoothDevice> = adapter.bondedDevices
                    // If there are paired devices
                    pairedDevices.forEach {
                        scanner.put(
                            it.address, BluetoothDeviceBean(
                                name = it.name,
                                address = it.address,
                                type = it.type,
                                bondState = it.bondState
                            )
                        )
                    }
                    bluetoothScanningResultCallback.onScan(scanner)
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    /**
     * 判断该设备是否连接成功
     */
    fun isConnectedDevice(
        address: String,
        isConnectedDeviceSuccessCallBack: IsConnectedDeviceSuccessCallBack
    ) {
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    isConnectedDeviceSuccessCallBack.isConnectedSuccess(
                        BluetoothDevice::class.java.getMethod("isConnected")
                            .invoke(getBAdapter().getRemoteDevice(address)) as Boolean
                    )
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    /**
     * 查找蓝牙，包括传统蓝牙和低功耗蓝牙
     *
     * 注：
     * 1.该方式在查找低功耗蓝牙上效率较低
     * 2.若只需要查找低功耗蓝牙，应该使用“低功耗蓝牙API”，即 findLE() 方法
     * 3.为防止非正常终止扫描造成的内存泄漏，使用该方法后，需在适当的时机，主动调用一次unRegisterBtScanReceiver()，以注销接收器
     *
     * @param scanInterval 扫描时长，单位：秒，建议取值范围(0,12]
     * @param bluetoothScanningResultCallback 扫描结果回调
     */
    @SuppressLint("MissingPermission")
    fun findLEAndClassic(
        scanInterval: Int,
        bluetoothScanningResultCallback: BluetoothScanningResultCallback
    ) {
        var scanInterval = scanInterval
        val scanner = mutableMapOf<String, BluetoothDeviceBean>()
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    if (!getBAdapter().isEnabled) {//没打开蓝牙
                        ToastUtils.showLong("蓝牙没打开")
                        return
                    }
                    if (getBAdapter().isDiscovering) {//已经在扫描中了
                        ToastUtils.showLong("已经在扫描中了")
                        return
                    }
                    // 默认扫描6秒，若scanInterval不合法，则使用默认值
                    val defaultInterval = 6
                    if (scanInterval <= 0) {
                        scanInterval = defaultInterval
                    }
                    // 通过bluetoothAdapter.startDiscovery()实现的扫描，系统会在扫描结束（通常是12秒）后自动停止，
                    // 而cancelDiscovery()可以提前终止扫描。 所以这里的控制逻辑，相当于设置一个最大时间，限制扫描不得超出这个时间，
                    // 但是很可能提前完成扫描（比如scanInterval > 12秒）
                    // 设置一段时间后停止扫描（以防系统未正常停止扫描）
                    val job = Job()
                    val coroutineScope = CoroutineScope(Dispatchers.Main + job)
                    coroutineScope.launch {
                        delay((scanInterval * 1000).toLong())
                        // 若已经停止扫描（系统扫描结束/通过cancelDiscovery取消扫描），
                        // 则再次调用该方法不会触发ACTION_DISCOVERY_FINISHED
                        getBAdapter().cancelDiscovery()
                        withContext(Dispatchers.Main) {
                            bluetoothScanningResultCallback.onScan(scanner)
                        }
                        job.cancel()
                    }
                    var bluetoothScanBroadcastReceiver =
                        BluetoothScanBroadcastReceiverListener(object :
                            BluetoothScanningResultCallback {
                            override fun onScan(result: MutableMap<String, BluetoothDeviceBean>) {
                                scanner.putAll(result)
                                if (job.isCancelled) {
                                    bluetoothScanningResultCallback.onScan(scanner)
                                } else {
                                    job.cancel()
                                }
                            }
                        })
                    bluetoothScanBroadcastReceiver.dynamicRegistration()
                    getBAdapter().startDiscovery()
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }


    /**
     * 查找低功耗蓝牙，该方法在4.3（API 18）以上，无法查找“传统蓝牙”
     * @param scanInterval 扫描时长，单位：秒
     * @param adapter
     * @param bluetoothScanningResultCallback 扫描结果回调
     */
    @SuppressLint("MissingPermission", "ObsoleteSdkInt")
    fun findLE(
        scanInterval: Int,
        bluetoothScanningResultCallback: BluetoothScanningResultCallback
    ) {
        var scanInterval = scanInterval
        val scanner = mutableMapOf<String, BluetoothDeviceBean>()
        try {
            BluetoothPermissionHelper.isBluetoothPermissionAndRequest(object :
                BluetoothPermissionRequestCallBack {
                override fun onGranted() {
                    if (!getBAdapter().isEnabled) {//没打开蓝牙
                        ToastUtils.showLong("蓝牙没打开")
                        return
                    }
                    if (getBAdapter().isDiscovering) {//已经在扫描中了
                        ToastUtils.showLong("已经在扫描中了")
                        return
                    }
                    val defaultInterval = 6 // 默认扫描6秒，若scanInterval不合法，则使用默认值
                    if (scanInterval <= 0) {
                        scanInterval = defaultInterval
                    }
                    if (Build.VERSION.SDK_INT < 18) {  //4.3的低功耗蓝牙API
                        findLEAndClassic(scanInterval, bluetoothScanningResultCallback)
                        return
                    }
                    // 5.0又引入了新的蓝牙API(4.3版本的API仍然可用)
                    if (Build.VERSION.SDK_INT < 21) {
                        val leScanCallBackListener =
                            LeScanCallBackListener(object : BluetoothScanningResultCallback {
                                override fun onScan(result: MutableMap<String, BluetoothDeviceBean>) {
                                    result.forEach {
                                        scanner.put(it.key, it.value)
                                    }
                                }
                            })
                        // 开始扫描
                        getBAdapter().startLeScan(leScanCallBackListener)
                        ToastUtils.showLong("开始扫描")
                        // 设置一段时间后停止扫描
                        val job = Job()
                        val coroutineScope = CoroutineScope(Dispatchers.Main + job)
                        coroutineScope.launch {
                            delay((scanInterval * 1000).toLong())
                            getBAdapter().stopLeScan(leScanCallBackListener)
                            withContext(Dispatchers.Main) {
                                bluetoothScanningResultCallback.onScan(scanner)
                            }
                            job.cancel()
                            ToastUtils.showLong("扫描完成")
                        }
                        return
                    }
                    //开始扫描
                    val mBLEScanner: BluetoothLeScanner = getBAdapter().bluetoothLeScanner

                    /** 也可指定过滤条件和扫描配置
                     * //创建ScanSettings的build对象用于设置参数
                     * ScanSettings.Builder builder = new ScanSettings.Builder()
                     * //设置高功耗模式
                     * .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
                     * //android 6.0添加设置回调类型、匹配模式等
                     * if(android.os.Build.VERSION.SDK_INT >= 23) {
                     * //定义回调类型
                     * builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
                     * //设置蓝牙LE扫描滤波器硬件匹配的匹配模式
                     * builder.setMatchMode(ScanSettings.MATCH_MODE_STICKY);
                     * }
                     * // 若设备支持批处理扫描，可以选择使用批处理，但此时扫描结果仅触发onBatchScanResults()
                     * //             if (bluetoothAdapter.isOffloadedScanBatchingSupported()) {
                     * //                 //设置蓝牙LE扫描的报告延迟的时间（以毫秒为单位）
                     * //                 //设置为0以立即通知结果
                     * //                 builder.setReportDelay(0L);
                     * //             }
                     * ScanSettings scanSettings = builder.build();
                     * //可设置过滤条件，在第一个参数传入，但一般不设置过滤。
                     * mBLEScanner.startScan(null, scanSettings, mScanCallback);
                     */
                    val systemBluetoothScanCallBack =
                        SystemBluetoothScanCallBack(object : BluetoothScanningResultCallback {
                            override fun onScan(result: MutableMap<String, BluetoothDeviceBean>) {
                                scanner.putAll(result)
                            }
                        })
                    mBLEScanner.startScan(systemBluetoothScanCallBack)
                    ToastUtils.showLong("开始扫描")
                    // 设置一段时间后停止扫描
                    val job = Job()
                    val coroutineScope = CoroutineScope(Dispatchers.Main + job)
                    coroutineScope.launch {
                        delay((scanInterval * 1000).toLong())
                        mBLEScanner.stopScan(systemBluetoothScanCallBack)
                        withContext(Dispatchers.Main) {
                            bluetoothScanningResultCallback.onScan(scanner)
                        }
                        job.cancel()
                        ToastUtils.showLong("扫描完成")
                    }
                }
            })
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun getBAdapter(): BluetoothAdapter {
        var adapter: BluetoothAdapter? = null
        try {
            adapter = if (Build.VERSION.SDK_INT >= 18) {
                val manager: BluetoothManager = Utils.getApp().applicationContext
                    ?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                manager.adapter
            } else {
                BluetoothAdapter.getDefaultAdapter()
            }
        } catch (t: Throwable) {
            LogUtils.i("BluetoothAdapter获取失败"+t.message)
        }
        return adapter!!
    }


}