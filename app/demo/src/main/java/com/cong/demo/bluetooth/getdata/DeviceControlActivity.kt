package com.cong.demo.bluetooth.getdata

import android.R
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.SimpleExpandableListAdapter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.bluetooth.BluetoothGattCallbackListener
import com.cong.demo.bluetooth.BluetoothLeService
import com.cong.demo.databinding.GattServicesCharacteristicsBinding
import java.util.*

class DeviceControlActivity : BaseActivity(){
    
    private lateinit var mBinding : GattServicesCharacteristicsBinding
    private var mBluetoothLeService: BluetoothLeService? = null
    private var mDeviceAddress: String? = null
    private var mConnected = false
    private var mGattCharacteristics =
        ArrayList<ArrayList<BluetoothGattCharacteristic>>()
    private var mNotifyCharacteristic: BluetoothGattCharacteristic? = null
    private val LIST_NAME = "NAME"
    private val LIST_UUID = "UUID"
    
    companion object{
        val EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS"
        fun go(address:String){
            val mBundle = Bundle()
            mBundle.putString(EXTRAS_DEVICE_ADDRESS, address)
           ActivityUtils.startActivity(mBundle,DeviceControlActivity::class.java)
        }
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            mBluetoothLeService = (service as BluetoothLeService.LocalBinder).service
            //成功启动初始化后自动连接
            mBluetoothLeService!!.connect(mDeviceAddress)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mBluetoothLeService = null
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = GattServicesCharacteristicsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        
        mDeviceAddress = intent.extras!!.getString(EXTRAS_DEVICE_ADDRESS)
        
        mBinding.deviceAddress.text = mDeviceAddress
        mBinding.connectionState.setOnClickListener {
            mBluetoothLeService?.let {
                if(mConnected){
                    mBluetoothLeService!!.disconnect()
                    ToastUtils.showLong("断开连接")
                }else{
                    mBluetoothLeService!!.connect(mDeviceAddress)
                    ToastUtils.showLong("重新连接")
                }
            }
        }

        val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
        bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private val mGattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothGattCallbackListener.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true
                updateConnectionState("连接")
                invalidateOptionsMenu()
            } else if (BluetoothGattCallbackListener.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false
                updateConnectionState("断开")
                invalidateOptionsMenu()
                clearUI()
            } else if (BluetoothGattCallbackListener.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService!!.getSupportedGattServices())
            } else if (BluetoothGattCallbackListener.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothGattCallbackListener.EXTRA_DATA))
            }
        }
    }

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private val servicesListClickListner =
        OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            if (mGattCharacteristics != null) {
                val characteristic: BluetoothGattCharacteristic =
                    mGattCharacteristics.get(groupPosition).get(childPosition)
                val charaProp = characteristic.properties
                if (charaProp or BluetoothGattCharacteristic.PROPERTY_READ > 0) {
                    // If there is an active notification on a characteristic, clear
                    // it first so it doesn't update the data field on the user interface.
                    if (mNotifyCharacteristic != null) {
                        mBluetoothLeService!!.setCharacteristicNotification(
                            mNotifyCharacteristic!!, false
                        )
                        mNotifyCharacteristic = null
                    }
                    mBluetoothLeService!!.readCharacteristic(characteristic)
                }
                if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                    mNotifyCharacteristic = characteristic
                    mBluetoothLeService!!.setCharacteristicNotification(
                        characteristic, true
                    )
                }
                return@OnChildClickListener true
            }
            false
        }

    private fun clearUI() {
        mBinding.tvSeiverList.text = ""
        mBinding.dataValue.setText("没有数据")
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (mBluetoothLeService != null) {
            mBluetoothLeService!!.connect(mDeviceAddress)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mGattUpdateReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
        mBluetoothLeService = null
    }

    private fun updateConnectionState(status: String) {
        runOnUiThread { mBinding.connectionState.setText(status) }
    }

    private fun displayData(data: String?) {
        if (data != null) {
            mBinding.dataValue.setText(data)
        }
    }

    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        mGattCharacteristics = ArrayList()
        var span = SpanUtils.with(mBinding.tvSeiverList)
        gattServices.forEach {
            span
                .appendLine("name:")
                .appendLine("type:"+it.type)
                .appendLine("uuid:"+it.uuid)
            it.characteristics.forEach {
                span
                    .appendLine("    特征权限:"+it.permissions)
                    .appendLine("    特征属性:"+it.properties)
                    .appendLine("    特征值:"+it.value)
                it.descriptors.forEachIndexed { index, bluetoothGattDescriptor ->
                    if (index == 0 ){
                        span.append("    特征描述:")
                    }
                    span
                        .appendLine(bluetoothGattDescriptor.describeContents().toString())
                }
            }
        }
        span.create()
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter? {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothGattCallbackListener.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothGattCallbackListener.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothGattCallbackListener.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothGattCallbackListener.ACTION_DATA_AVAILABLE)
        return intentFilter
    }
}