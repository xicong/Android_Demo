package com.wyl.monitor.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;

import java.util.List;

public class ViseBle {

    private static final String TAG = ViseBle.class.getName();
    private Context mContext;//上下文
    private BluetoothManager manager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBLEScanner;
    private MyLeScanCallback mLeScanCallback;
    private MyScanCallback mScanCallback;
    private OnScanResultListener onScanResultListener;

    private static ViseBle instance;//入口操作管理

    private ViseBle() {
    }

    public static ViseBle getInstance() {
        if (instance == null) {
            synchronized (ViseBle.class) {
                if (instance == null) {
                    instance = new ViseBle();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        this.mContext = context;
         manager = (BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        mLeScanCallback = new MyLeScanCallback();
        mScanCallback = new MyScanCallback();
    }

    //开始扫描
    public void startLeScan(){
        //开始扫描
        if(Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }else{
        //获取 5.0 的扫描类实例
            mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        //开始扫描
        //可设置过滤条件，在第一个参数传入，但一般不设置过滤。
            mBLEScanner.startScan(mScanCallback);
        }
    }

    //停止扫描
    public void stopScan(){
        //开始扫描
        if(Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }else{
            //获取 5.0 的扫描类实例
            if(mBLEScanner!=null)
            mBLEScanner.stopScan(mScanCallback);
        }
    }




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    class MyScanCallback extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //扫描类型有开始扫描时传入的ScanSettings相关
            //对扫描到的设备进行操作。如：获取设备信息。
            if(onScanResultListener!=null){
                onScanResultListener.onScanResult(result.getDevice(),result.getRssi());
            }
        }

        //批量返回扫描结果
        //@param results 以前扫描到的扫描结果列表。
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

        }

        //当扫描不能开启时回调
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            //扫描太频繁会返回ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED，表示app无法注册，无法开始扫描。
            if(onScanResultListener!=null){
                onScanResultListener.onScanFailed(errorCode);
            }
        }
    }


    class MyLeScanCallback implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(onScanResultListener!=null){
                onScanResultListener.onScanResult(device,rssi);
            }
        }
    }
    
    public void setOnScanResultListener(OnScanResultListener onScanResultListener) {
        this.onScanResultListener = onScanResultListener;
    }

    public interface OnScanResultListener{
        void onScanResult(BluetoothDevice bluetoothDevice, int rssi);
        void onScanFailed(int errorCode);//扫描出错
    }

}
