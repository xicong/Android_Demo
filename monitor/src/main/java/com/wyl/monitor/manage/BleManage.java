package com.wyl.monitor.manage;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.wyl.monitor.analysis.PillowDevice;
import com.wyl.monitor.base.BaseDevice;
import com.wyl.monitor.base.MyBluetoothGattCallback;
import com.wyl.monitor.base.OnBaseBleGattCallback;
import com.wyl.monitor.util.ViseBle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BleManage {

    private final String TAG = BleManage.class.getName();
    private Context context;
    private volatile Map<String, BaseDevice> baseDeviceMap = new HashMap<>();
    private static volatile BleManage bleManage;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private OnBaseBleGattCallback onBaseBleGattCallback;

    // 蓝牙已连接
    public final static String ACTION_GATT_CONNECTED = "com.wyl.recognizer.ACTION_GATT_CONNECTED";
    // 蓝牙已断开
    public final static String ACTION_GATT_DISCONNECTED = "com.wyl.recognizer.ACTION_GATT_DISCONNECTED";
    // 发现GATT服务
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.wyl.recognizer.ACTION_GATT_SERVICES_DISCOVERED";
    // 连接失败
    public final static String ACTION_CONNECTING_FAIL = "com.wyl.recognizer.ACTION_CONNECTING_FAIL";
    //ble数据
    public final static String ACTION_DATA_AVAILABLE = "com.wyl.ble.ACTION_DATA_AVAILABLE";

    private BleManage() {

    }

    public static BleManage getInstance() {
        if (bleManage == null) {
            synchronized (BleManage.class) {
                if (bleManage == null) {
                    bleManage = new BleManage();
                }
            }
        }
        return bleManage;
    }

    public void init(Context context) {
        this.context = context;
        mBluetoothManager = (BluetoothManager) context.getSystemService(context.BLUETOOTH_SERVICE);//获取蓝牙服务
        mBluetoothAdapter = mBluetoothManager.getAdapter();//拿到适配器
        ViseBle.getInstance().init(context);
    }

    public void setOnBaseBleGattCallback(OnBaseBleGattCallback onBaseBleGattCallback) {
        this.onBaseBleGattCallback = onBaseBleGattCallback;
    }

    /**
     * 蓝牙连接
     *
     * @param Mac
     */
    public void ConnectDevice(String Mac) {
        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(Mac);
        if (bluetoothDevice == null)
            return;
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
        //连接设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt = bluetoothDevice.connectGatt(context,
                    false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            mBluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
        }
    }


    /**
     * 断开连接
     */
    public void DisconnectDevice() {
        BaseDevice baseDevice = getBaseDevice();
        if(baseDevice!=null) {
            baseDevice.mBluetoothGatt.disconnect();
            baseDevice.mBluetoothGatt.close();
            sendMessage(ACTION_GATT_DISCONNECTED,null,null,baseDevice.getMac());
        }
    }

    /**
     * 断开连接
     */
    public void DisconnectDevice1() {
        BaseDevice baseDevice = getBaseDevice();
        if(baseDevice!=null) {
            baseDevice.mBluetoothGatt.close();
            sendMessage(ACTION_GATT_DISCONNECTED,null,null,baseDevice.getMac());
        }
    }

    /**
     * 断开连接
     */
    public void closeConnectDevice(String mac) {
        sendMessage(ACTION_GATT_DISCONNECTED,null,null,mac);
    }


    public MyBluetoothGattCallback bluetoothGattCallback = new MyBluetoothGattCallback() {

        PillowDevice sleepWithDevice;

        @Override //连接状态返回
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            //操作成功的情况下
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //判断是否连接码
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //可延迟发现服务，也可不延迟
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                               Thread.sleep(500);
                                gatt.discoverServices();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    sleepWithDevice = new PillowDevice(context, mBluetoothGatt);
                    Log.e(TAG, "连接成功");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    //判断是否断开连接码
                    //sendBroadcastMessage(ACTION_GATT_DISCONNECTED);//断开连接发送广播通知
                    sendMessage(ACTION_GATT_DISCONNECTED,gatt,null,gatt.getDevice().getAddress());
                    Log.e(TAG, "连接断开");
                }
            } else {
                /* status 用于返回操作是否成功,会返回异常码。
                  133 ：连接超时或未找到设备。
                  8 ： 设备超出范围
                  22 ：表示本地设备终止了连接
                */
                mBluetoothGatt.close();
                //sendBroadcastMessage(ACTION_CONNECTING_FAIL);//连接失败发送广播通知,重新连接
                switch (status){
                    case 133:
                        Log.e(TAG, "断开连接");
                        sendMessage(ACTION_CONNECTING_FAIL,gatt,null,gatt.getDevice().getAddress());
                        break;
                     default:
                         Log.e(TAG, "连接失败，重新连接");
                         sendMessage(ACTION_GATT_DISCONNECTED,gatt,null,gatt.getDevice().getAddress());
                         break;
                }

            }
        }


        @Override   //服务特性返回
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService bluetoothGattService = gatt.getService(sleepWithDevice.getServiceUUID());

                if (bluetoothGattService != null && sleepWithDevice != null) {
                    final BluetoothGattCharacteristic notify = bluetoothGattService.getCharacteristic(sleepWithDevice.getNotifyUUID());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                //开启通知
                                if (mBluetoothGatt.setCharacteristicNotification(notify, true)) {
                                    List<BluetoothGattDescriptor> descriptorList = notify.getDescriptors();
                                    if(descriptorList != null && descriptorList.size() > 0) {
                                        for(BluetoothGattDescriptor descriptor : descriptorList) {
                                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                            mBluetoothGatt.writeDescriptor(descriptor);
                                        }
                                    }
                                    // sendBroadcastMessage(ACTION_GATT_CONNECTED);//连接成功发送广播通知
                                    sendMessage(ACTION_GATT_CONNECTED,gatt,sleepWithDevice,sleepWithDevice.getMac());
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                } else {
                    //获取特定服务失败
                    sendMessage(ACTION_GATT_DISCONNECTED,gatt,null,gatt.getDevice().getAddress());
                    Log.e(TAG, "获取特定服务失败，断开连接.");
                }
            }
        }


        @Override //当成功读取特征值时回调
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //获取写入到外设的特征值
                byte[] writeValue = characteristic.getValue();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < writeValue.length; i++) {
                    sb.append(String.format("%02x ", writeValue[i]));
                }
                Log.i("onCharacteristicWrite", sb.toString());
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
            if (baseDeviceMap.size() > 0) {
                List<BaseDevice> baseDeviceList = getAllBaseDevice();
                for (BaseDevice baseDevice : baseDeviceList) {
                    try {
                        if (gatt.getDevice().getAddress() == baseDevice.mBluetoothGatt.getDevice().getAddress()){
                            baseDevice.CallbackData(data, characteristic);
                            if (onBaseBleGattCallback != null)
                                onBaseBleGattCallback.onBackData(baseDevice,data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 返回连接状态
     */
    private void sendMessage(String action , BluetoothGatt gatt, PillowDevice pillowDevice, String mac) {
            switch (action) {
                case ACTION_GATT_CONNECTED:
                    //保存当前连接设备对象
                    baseDeviceMap.put(mac, pillowDevice);
                    if (onBaseBleGattCallback != null)
                        onBaseBleGattCallback.onConnectSuccess(gatt,pillowDevice);
                   /* pillowDevice.sendInquireVersions();*/
                    break;

                case ACTION_GATT_DISCONNECTED:
                    //断开连接，关掉获取实时数据，删除设备
                    String address;
                    if (gatt != null) {
                        address = gatt.getDevice().getAddress();
                    } else {
                        address = mac;
                    }
                    BaseDevice baseDevice = baseDeviceMap.get(address);
                    if (baseDevice!= null) {
                        //baseDevice.mBluetoothGatt.close();
                        baseDeviceMap.remove(address);
                    }
                    if (onBaseBleGattCallback != null)
                        onBaseBleGattCallback.onDisconnect(gatt);
                    break;
                case ACTION_CONNECTING_FAIL:

                    if (gatt != null) {
                        gatt.disconnect();
                        gatt.close();
                    }
                    if (onBaseBleGattCallback != null)
                        onBaseBleGattCallback.onConnectFailure(gatt);
                    break;
            }
    }

    /**
     * 获取连接的设备
     *
     * @return
     */
    public BaseDevice getBaseDevice(String mac) {
        BaseDevice baseDevice = baseDeviceMap.get(mac);
        return baseDevice;
    }

    /**
     * 获取连接的设备
     *
     * @return
     */
    /**
     * 获取连接的设备
     * @return
     */
    public BaseDevice getBaseDevice(){
        Iterator<Map.Entry<String, BaseDevice>> entries = baseDeviceMap.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<String, BaseDevice> entry = entries.next();
            String key = entry.getKey();
            BaseDevice value = entry.getValue();
            return value;
        }
        return null;
    }

    /**
     * 获取连接的设备
     *
     * @return
     */
    public List<BaseDevice> getAllBaseDevice() {
        List<BaseDevice> baseDeviceList = new ArrayList<>();
        Iterator<Map.Entry<String, BaseDevice>> entries = baseDeviceMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, BaseDevice> entry = entries.next();
            String key = entry.getKey();
            BaseDevice value = entry.getValue();
            if (value != null) {
                baseDeviceList.add(value);
            }
        }
        return baseDeviceList;
    }

}
