package com.wyl.monitor.base;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.wyl.monitor.analysis.PillowDevice;

public class MyBluetoothGattCallback extends BluetoothGattCallback {

    private final String TAG = MyBluetoothGattCallback.class.getName();

    private PillowDevice pillowDevice;

    @Override //连接状态返回
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        //操作成功的情况下
        if (status == BluetoothGatt.GATT_SUCCESS) {

        } else {

        }
    }

    @Override   //服务特性返回
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

    }


    @Override //当成功读取特征值时回调
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //获取写入到外设的特征值
            byte[] writeValue = characteristic.getValue();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<writeValue.length; i++){
                sb.append(String.format("%02x ",writeValue[i]));
            }
            Log.i("onCharacteristicWrite",sb.toString());
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        //从蓝牙里读取数据的回调
        Log.i(TAG, "onCharacteristicRead: ");
        if (status == BluetoothGatt.GATT_SUCCESS) {

        }
    }


    /**
     * 通知返回数据
     * @param gatt
     * @param characteristic
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        //获取写入到外设的特征值
        byte[] data = characteristic.getValue();
        //Log.i("onCharacteristicChanged",HexString.toHexString(data,0,data.length));
        if(pillowDevice !=null){
            try {
                pillowDevice.CallbackData(data,characteristic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
