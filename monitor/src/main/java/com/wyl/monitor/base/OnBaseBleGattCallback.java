package com.wyl.monitor.base;

import android.bluetooth.BluetoothGatt;

public class OnBaseBleGattCallback implements BaseBleGattCallbackListener{
    @Override
    public void onConnectSuccess(BluetoothGatt gatt,BaseDevice baseDevice) {

    }

    @Override
    public void onConnectFailure(BluetoothGatt gatt) {

    }

    @Override
    public void onDisconnect(BluetoothGatt gatt) {

    }

    @Override
    public void onBluetoothClosed() {

    }

    @Override
    public void onBackData(BaseDevice baseDevice,byte[] data) {

    }

}
