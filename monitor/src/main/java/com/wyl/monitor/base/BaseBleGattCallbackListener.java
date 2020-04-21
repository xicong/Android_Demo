package com.wyl.monitor.base;

import android.bluetooth.BluetoothGatt;

public interface BaseBleGattCallbackListener {
    //连接成功
    void onConnectSuccess(BluetoothGatt gatt,BaseDevice baseDevice);

    //连接失败
    void onConnectFailure(BluetoothGatt gatt);

    //连接断开
    void onDisconnect(BluetoothGatt gatt);

    //手机蓝牙关闭
    void onBluetoothClosed();

    //通知返回的数据
    void onBackData(BaseDevice baseDevice,byte[] data);
}
