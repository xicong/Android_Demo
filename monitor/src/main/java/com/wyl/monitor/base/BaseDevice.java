package com.wyl.monitor.base;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.wyl.monitor.analysis.OnRealTimeDataListener;
import com.wyl.monitor.entity.pneumatic_bed.RealTimeRedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class BaseDevice {

    public BluetoothGatt mBluetoothGatt;
    private String mac = "";
    private Context mContext;
    private OnRealTimeDataListener onRealTimeDataListener;//实时数据监听
    private Set<OnRealTimeDataListener> realTimeDataListenerSet = new HashSet<>();

    public BaseDevice(Context context,BluetoothGatt bluetoothGatt){
        this.mBluetoothGatt = bluetoothGatt;
        this.mac = bluetoothGatt.getDevice().getAddress();
        this.mContext = context;
    }

    public void setOnRealTimeDataListeners(Set<OnRealTimeDataListener> onRealTimeDataListenerSet) {
        realTimeDataListenerSet.clear();
        realTimeDataListenerSet.addAll(onRealTimeDataListenerSet);
    }

    public void setOnRealTimeDataListener(OnRealTimeDataListener onRealTimeDataListener) {
        this.onRealTimeDataListener = onRealTimeDataListener;
    }

    public OnRealTimeDataListener getOnRealTimeDataListener() {
        return onRealTimeDataListener;
    }

    public abstract UUID getServiceUUID();

    public abstract UUID getNotifyUUID();

    public abstract UUID getWriteUUID();

    public abstract UUID getReadUUID();

    public String getMac() {
        return mac;
    }

    /**
     * 蓝牙通知返回数据
     * @param datas
     */
    public abstract void  CallbackData(byte[] datas,BluetoothGattCharacteristic characteristic);

    /**
     * 发送数据
     * @param datas
     */
    public boolean sendData(byte[] datas){
        if(mBluetoothGatt!=null && mBluetoothGatt.connect()){
            BluetoothGattCharacteristic bluetoothGattCharacteristic =  mBluetoothGatt.getService(getServiceUUID()).getCharacteristic(getWriteUUID());
            //BluetoothGattCharacteristic bluetoothGattCharacteristic =  bluetoothGattService.getCharacteristic(getWriteUUID());
            bluetoothGattCharacteristic.setValue(datas);
            bluetoothGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
           return mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }
        return false;
    }

    /**
     * 返回解析后的实时数据
     */
    public void  OnRealTimeData(RealTimeRedData realTimeRedData){
       if(onRealTimeDataListener!=null)
            onRealTimeDataListener.OnRealTimeData(realTimeRedData);
        }


    }
