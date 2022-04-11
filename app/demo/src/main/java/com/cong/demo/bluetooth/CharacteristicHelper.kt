package com.cong.demo.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

/**
 * 一般协议中会指出特征读可读可写性的
 */
object CharacteristicHelper {

    /**
     * 判断特征可读
     */
    fun isCharacteristicRedable(bluetoothGattCharacteristic: BluetoothGattCharacteristic): Boolean {
        return (bluetoothGattCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ) > 0
    }

    /**
     * 判断特征可写
     */
    fun isCharacteristicWritable(bluetoothGattCharacteristic: BluetoothGattCharacteristic): Boolean {
        return (bluetoothGattCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE) > 0
                || (bluetoothGattCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0
    }

    /**
     * 判断特征是否具有通知读属性
     */
    fun isCharacteristicNotifiable(bluetoothGattCharacteristic: BluetoothGattCharacteristic): Boolean {
        return (bluetoothGattCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
                || (bluetoothGattCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0
    }

    /**
     * 读取特征
     * 读取结果会回调到mGattCallBack的onCharacteristicRead中
     */
    fun readCharacteristic(
        bluetoothGatt: BluetoothGatt,
        bluetoothGattCharacteristic: BluetoothGattCharacteristic
    ) {
        bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic)
    }

    /**
     * 写入特征
     * 写入特征结果会回调到mGattCallBack的onCharacteristicWrite中
     */
    fun writeCharacteristic(
        bluetoothGatt: BluetoothGatt,
        bluetoothGattCharacteristic: BluetoothGattCharacteristic
    ) {
//        bluetoothGattCharacteristic.setValue("")   可以是字符串和数组
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic)
    }

    /**
     * 设置通知
     * 结果会回调到mGattCallBack的onCharacteristicChange中
     */
    fun setCharacterNotfication(
        bluetoothGatt: BluetoothGatt,
        bluetoothGattCharacteristic: BluetoothGattCharacteristic,
        enable: Boolean
    ) {
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable)
        //以下代码建议写上
        //在明确知道当前特征的描述符前提下，可以直接使用描述符，不需要判断
        //但是如果不知道此特征是否有描述符的前提下，没有以下几行代码可能会导致设置通知失败的情况发生
        var descriptorsList = bluetoothGattCharacteristic.descriptors
        descriptorsList.forEach {
            var value =
                if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            it.value = value
            bluetoothGatt.writeDescriptor(it)
        }
    }

}