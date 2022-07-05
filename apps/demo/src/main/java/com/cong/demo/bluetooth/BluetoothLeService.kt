package com.cong.demo.bluetooth

import android.app.Service
import android.bluetooth.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import java.util.*

/**
 * 蓝牙连接
 */
class BluetoothLeService : Service() {
    
    private var mBluetoothAdapter: BluetoothAdapter = BluetoothHelper.getBAdapter()
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothGatt: BluetoothGatt? = null

    private final val mBinder: IBinder = LocalBinder()
    private val bluetoothGattCallbackListener = BluetoothGattCallbackListener(this)
    
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }
    
    /**
     * 内部类关键字
     */
    inner  class LocalBinder : Binder() {
        val service: BluetoothLeService
            get() {
                return this@BluetoothLeService
            }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources arereleased properly.
     * 使用完给定的BLE资源后，必须调用此方法，以保证资源的正常释放
     */
    fun close() {
        mBluetoothGatt!!.close()
        mBluetoothGatt = null
    }

    /**
     * 连接GATT服务器
     * @param address The device address of the destination device.
     * @return 判断是否连接成功
     */
    fun connect(address: String?): Boolean {
        if (mBluetoothAdapter == null || address == null) {
            LogUtils.i("蓝牙适配器初始化失败或者地址异常")
            return false
        }
        // 之前连接过的设备尝试重新连接
        if (mBluetoothDeviceAddress != null && address == mBluetoothDeviceAddress && mBluetoothGatt != null) {
            LogUtils.i("尝试使用现有的 BluetoothGatt重新连接")
            return if (mBluetoothGatt!!.connect()) {
                BluetoothGattCallbackListener.mConnectionState = BluetoothGattCallbackListener.STATE_CONNECTING
                LogUtils.i("尝试使用现有的 BluetoothGatt重新连接返回true:"+ BluetoothDevice::class.java.getMethod("isConnected")
                    .invoke(mBluetoothAdapter!!.getRemoteDevice(address)) as Boolean)
                true
            } else {
                LogUtils.i("尝试使用现有的 BluetoothGatt重新连接返回false")
                false
            }
        }
        val device = mBluetoothAdapter!!.getRemoteDevice(address)
        if (device == null) {
            LogUtils.i("未找到设备，无法连接")
            return false
        }
        mBluetoothGatt = device.connectGatt(this, false, bluetoothGattCallbackListener)
        LogUtils.i("尝试创建一个新的连接")
        mBluetoothDeviceAddress = address
        BluetoothGattCallbackListener.mConnectionState = BluetoothGattCallbackListener.STATE_CONNECTING
        return true
    }

    /**
     *断开当前连接或者取消或者挂起
     */
    fun disconnect() {
        mBluetoothGatt!!.disconnect()
    }

    /**
     *读取特征
     * @param characteristic The characteristic to read from.
     */
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        mBluetoothGatt!!.readCharacteristic(characteristic)
    }

    /**
     * 启动或禁用特征的通知
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        mBluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)
        // This is specific to Heart Rate Measurement.
        if (bluetoothGattCallbackListener.UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            val descriptor = characteristic.getDescriptor(
                UUID.fromString(bluetoothGattCallbackListener.CLIENT_CHARACTERISTIC_CONFIG)
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            mBluetoothGatt!!.writeDescriptor(descriptor)
        }
    }

    /**
     * 获取连接上GATT服务的列表
     * 仅在BluetoothGatt#discoverServices()` 成功完成后调用
     *
     * @return A `List` of supported services.
     */
    fun getSupportedGattServices(): List<BluetoothGattService>{
        return (if (mBluetoothGatt == null) null else mBluetoothGatt!!.services) as List<BluetoothGattService>
    }
    
}

