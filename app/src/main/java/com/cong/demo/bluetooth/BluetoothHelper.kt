package com.cong.demo.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.*
import kotlinx.coroutines.*


object BluetoothHelper {

    private var mScanning = false  //标记是否在扫描中  true 扫描中  false 没有扫描
    private var mScanningBle = false  //标记是否在扫描中  true 扫描中  false 没有扫描

    /**
     * 打开蓝牙
     */
    @SuppressLint("MissingPermission")
    fun enabledBluetooth(bluetoothOnCallBack: BluetoothOnCallBack) {
        try {
            if (!PermissionUtils.isGranted(
                    "android.permission.BLUETOOTH",
                    "android.permission.BLUETOOTH_ADMIN"
                )
            ) {
                ToastUtils.showLong("没有权限")
                return
            }
            val bluetoothOnBroadcastReceiver =
                BluetoothOnBroadcastReceiver(object : BluetoothOnCallBack {
                    override fun onOn() {
                        super.onOn()
                        bluetoothOnCallBack.onOn()
                    }
                })
            bluetoothOnBroadcastReceiver.dynamicRegistration()
            getBAdapter()!!.enable()
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
    fun disableBluetooth(bluetoothOff: BluetoothOffCallBack) {
        try {
            if (!PermissionUtils.isGranted(
                    "android.permission.BLUETOOTH",
                    "android.permission.BLUETOOTH_ADMIN"
                )
            ) {
                ToastUtils.showLong("没有权限")
                return
            }
            val bluetoothOffBroadcastReceiver =
                BluetoothOffBroadcastReceiver(object : BluetoothOffCallBack {
                    override fun onOff() {
                        super.onOff()
                        bluetoothOff.onOff()
                    }
                })
            bluetoothOffBroadcastReceiver.dynamicRegistration()
            getBAdapter()!!.disable()
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    /**
     * 判断设备是否支持蓝牙
     */
    fun isSupportBluetooth(): Boolean {
        try {
            if (!PermissionUtils.isGranted("android.permission.BLUETOOTH")) {
                ToastUtils.showLong("没有权限")
                return false
            }
            return getBAdapter() != null
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
        return false
    }

    /**
     * 判断蓝牙是否已打开
     *
     * @return  true 打开  false 未打开
     */
    @SuppressLint("MissingPermission")
    fun isBluetoothEnabled(): Boolean {
        try {
            if (!PermissionUtils.isGranted("android.permission.BLUETOOTH")) {
                ToastUtils.showLong("没有权限")
                return false
            }
            return getBAdapter().isEnabled
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
        return false
    }


    /**
     * 查询已配对的蓝牙设备
     */
    @SuppressLint("MissingPermission")
    fun getBondedDevice(): MutableMap<String, HashMap<String, Any>> {
        val scanner = mutableMapOf<String, HashMap<String, Any>>()
        try {
            if (!PermissionUtils.isGranted(
                    "android.permission.BLUETOOTH",
                    "android.permission.BLUETOOTH_ADMIN"
                )
            ) {
                ToastUtils.showLong("没有蓝牙权限")
                scanner.clear()
                return scanner
            }
            val adapter: BluetoothAdapter = getBAdapter()
            val pairedDevices: Set<BluetoothDevice> = adapter.bondedDevices
            // If there are paired devices
            pairedDevices.forEach {
                val deviceInfo: HashMap<String, Any> = parseDevice2Map(it)
                deviceInfo["__currConnected"] = if (isConnectedDevice(it)) 1 else 0
                scanner[deviceInfo["address"].toString()] = deviceInfo
            }
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
        return scanner
    }

    /**
     * 判断该设备是否连接成功
     */
    fun isConnectedDevice(device: BluetoothDevice?): Boolean {
        device.let {
            try {
                if (!PermissionUtils.isGranted("android.permission.BLUETOOTH")) {
                    ToastUtils.showLong("没有权限")
                    return false
                }
                return ReflectUtils.reflect(it).method("isConnected").get()
            } catch (t: Throwable) {
                LogUtils.i(t.message + "")
            }
        }
        return false
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
     * @param bluetoothScanCallback 扫描结果回调
     */
    @SuppressLint("MissingPermission")
    fun findLEAndClassic(scanInterval: Int, bluetoothScanCallback: BluetoothScanCallback) {
        var scanInterval = scanInterval
        val scanner = mutableMapOf<String, HashMap<String, Any>>()
        try {
            if (!PermissionUtils.isGranted(
                    "android.permission.BLUETOOTH",
                    "android.permission.BLUETOOTH_ADMIN"
                )
            ) {
                ToastUtils.showLong("没有蓝牙权限")
                return
            }
            BluetoothPermissionUtils.bluetoothPermissionProcess(object :
                BluetoothPermissionCallBack {
                override fun onGranted() {
                    if (!isBluetoothEnabled()) {  // 若蓝牙未打开，直接返回
                        ToastUtils.showLong("蓝牙没打开")
                        return
                    }
                    if (mScanning) {   // 正在扫描中
                        ToastUtils.showLong("正在扫描中")
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
                        mScanningBle = false
                        // 若已经停止扫描（系统扫描结束/通过cancelDiscovery取消扫描），
                        // 则再次调用该方法不会触发ACTION_DISCOVERY_FINISHED
                        getBAdapter().cancelDiscovery()
                        withContext(Dispatchers.Main) {
                            bluetoothScanCallback.onScan(scanner)
                        }
                        job.cancel()
                    }

                    var bluetoothScanBroadcastReceiver =
                        BluetoothScanBroadcastReceiver(object : BluetoothScanCallback {
                            override fun onScan(result: MutableMap<String, HashMap<String, Any>>) {
                                scanner.putAll(result)
                                mScanning = false
                                if (job.isCancelled) {
                                    bluetoothScanCallback.onScan(scanner)
                                } else {
                                    job.cancel()
                                }
                            }
                        })
                    bluetoothScanBroadcastReceiver.dynamicRegistration()
                    // 开始扫描
                    mScanning = true
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
     * @param bluetoothScanCallback 扫描结果回调
     */
    @SuppressLint("MissingPermission", "ObsoleteSdkInt")
    fun findLE(
        scanInterval: Int,
        bluetoothScanCallback: BluetoothScanCallback
    ) {
        var scanInterval = scanInterval
        val scanner = mutableMapOf<String, HashMap<String, Any>>()
        try {
            if (!PermissionUtils.isGranted(
                    "android.permission.BLUETOOTH",
                    "android.permission.BLUETOOTH_ADMIN"
                )
            ) {
                ToastUtils.showLong("没有蓝牙权限")
                return
            }
            if (!getBAdapter().isEnabled) {//没打开蓝牙
                ToastUtils.showLong("蓝牙没打开")
                return
            }
            if (mScanningBle) {//已经在扫描中了
                ToastUtils.showLong("正在扫描中")
                return
            }
            // 默认扫描6秒，若scanInterval不合法，则使用默认值
            val defaultInterval = 6
            if (scanInterval <= 0) {
                scanInterval = defaultInterval
            }
            // 4.3的低功耗蓝牙API
            if (Build.VERSION.SDK_INT < 18) {
                findLEAndClassic(scanInterval, bluetoothScanCallback)
                return
            }
            // 5.0又引入了新的蓝牙API(4.3版本的API仍然可用)
            if (Build.VERSION.SDK_INT < 21) {
                // 定义扫描结果回调
                val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
                    /**
                     * @param device 扫描到的设备实例，可从实例中获取到相应的信息。如：名称，mac地址
                     * @param rssi 可理解成设备的信号值。该数值是一个负数，越大则信号越强
                     * @param scanRecord 远程设备提供的广播数据的内容
                     */
                    try {
                        val map: HashMap<String, Any> = parseDevice2Map(device)
                        map["rssi"] = rssi
                        scanner.put(map.get("address").toString(), map)
                    } catch (t: Throwable) {
                        LogUtils.i(t.message + "")
                    }
                }
                // 开始扫描
                mScanningBle = true
                getBAdapter().startLeScan(leScanCallback)
                // 设置一段时间后停止扫描
                val job = Job()
                val coroutineScope = CoroutineScope(Dispatchers.Main + job)
                coroutineScope.launch {
                    delay((scanInterval * 1000).toLong())
                    mScanningBle = false
                    getBAdapter().stopLeScan(leScanCallback)
                    withContext(Dispatchers.Main) {
                        bluetoothScanCallback.onScan(scanner)
                    }
                    job.cancel()
                }
                return
            }

            //开始扫描
            val mBLEScanner: BluetoothLeScanner = getBAdapter().bluetoothLeScanner
            mScanningBle = true
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
                SystemBluetoothScanCallBack(object : BluetoothScanCallback {
                    override fun onScan(result: MutableMap<String, HashMap<String, Any>>) {
                        scanner.putAll(result)
                    }
                })
            
            mBLEScanner.startScan(systemBluetoothScanCallBack)
            // 设置一段时间后停止扫描
            val job = Job()
            val coroutineScope = CoroutineScope(Dispatchers.Main + job)
            coroutineScope.launch {
                delay((scanInterval * 1000).toLong())
                mScanningBle = false
                mBLEScanner.stopScan(systemBluetoothScanCallBack)
                withContext(Dispatchers.Main) {
                    bluetoothScanCallback.onScan(scanner)
                }
                job.cancel()
            }
        } catch (t: Throwable) {
            LogUtils.i(t.message + "")
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun getBAdapter(): BluetoothAdapter {
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
            LogUtils.i(t.message + "")
        }
        return adapter!!
    }

    @SuppressLint("MissingPermission", "ObsoleteSdkInt")
    fun parseDevice2Map(device: BluetoothDevice?): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        if (device != null) {
            try {
                if(!device.name.isNullOrEmpty()){
                    map["name"] = device.name
                }
                if (!device.address.isNullOrEmpty()){
                    map["address"] = device.address
                }
                map["bondState"] = device.bondState
                val btClass: BluetoothClass = device.bluetoothClass
                val majorClass: Int = btClass.majorDeviceClass
                val deviceClass: Int = btClass.deviceClass
                map["majorClass"] = majorClass
                map["deviceClass"] = deviceClass
                if (Build.VERSION.SDK_INT >= 18) {
                    map["type"] = device.type
                }
                // 已配对的设备，同时获取其uuids
                if (Build.VERSION.SDK_INT < 15 && device.bondState != 12) {
                    return map
                }
                val uuids = ArrayList<String>()
                device.uuids.forEach {
                    uuids.add(it.uuid.toString())
                }
                map["uuids"] = uuids
            } catch (t: Throwable) {
                LogUtils.i(t.message + "")
            }
        }
        return map
    }


}