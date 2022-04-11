package com.cong.demo.bluetooth

import android.bluetooth.*
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import java.util.*

/**
 * 连接的回调监听
 */
class BluetoothGattCallbackListener(val service: BluetoothLeService) : BluetoothGattCallback() {
    
    final val UUID_HEART_RATE_MEASUREMENT =
        UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
    final var CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"

    companion object{
        final val STATE_DISCONNECTED = 0
        final val STATE_CONNECTING = 1
        final val STATE_CONNECTED = 2

        final val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
        final var mConnectionState: Int = STATE_DISCONNECTED
        final val ACTION_GATT_CONNECTED = "com.cong.demo.bluetooth.ACTION_GATT_CONNECTED"
        final val ACTION_GATT_DISCONNECTED =
            "com.cong.demo.bluetooth.ACTION_GATT_DISCONNECTED"
        final val ACTION_GATT_SERVICES_DISCOVERED =
            "com.cong.demo.bluetooth.ACTION_GATT_SERVICES_DISCOVERED"
        final val ACTION_DATA_AVAILABLE = "com.cong.demo.bluetooth.le.ACTION_DATA_AVAILABLE"
    }

    //当设备与中心连接状态发生改变时
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
//        super.onConnectionStateChange(gatt, status, newState)
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                when(newState){
                    BluetoothProfile.STATE_CONNECTED -> {
                        //已经连接成功 如果成功连接  我们会执行discoverServices()方法去发现设备所包含当服务
                        mConnectionState = STATE_CONNECTED
                        broadcastUpdate(ACTION_GATT_CONNECTED)
                        LogUtils.i("已连接到GATT服务器")
                        // Attempts to discover services after successful connection.
                        //成功连接后尝试发现服务
                        LogUtils.i("尝试启动服务发现：${gatt!!.discoverServices()}")
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        //gatt连接已断开
                        mConnectionState = STATE_DISCONNECTED
                        LogUtils.i("与GATT服务器断开连接")
                        broadcastUpdate(ACTION_GATT_DISCONNECTED)
                    }
                }
            }
            else ->{
                LogUtils.i("连接失败  status =${status}     newState = ${newState}")
//                gatt!!.disconnect()
//                gatt!!.close()
            }
        }
    }

    //当发现设备服务时
    //执行discoverServices后 外设就会告诉我们能够为中心提供那些服务
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
//        super.onServicesDiscovered(gatt, status)
        when (status) {  //newState当前最新状态  status之前当状态
            BluetoothGatt.GATT_SUCCESS -> {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
//                gatt.getServices()  获取外设能提供当所有服务
//                gatt?.services?.forEach {
//                    //每发现一个服务再遍历服务当中当特征
//                    it.characteristics.forEach {  //遍历所有特征 
//                        bluetoothGattCharacteristicList.add(it)
//                        LogUtils.i("打印特征当uuid:${it.uuid}")
//                    }
//                }
            }
            else -> {
                LogUtils.i("onServicesDiscovered received: " + status)
            }
        }
        //当方法执行完后，我们就能得到了设备当所有特征
        //如果你想知道每个特征包含那些描述符，在用循环去遍历每一个特征当getDescripor()
    }

    //读取特征后回调这里  执行redCharacteristic() 方法后 结果就在这里
    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
//        super.onCharacteristicRead(gatt, characteristic, status)
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                //如果程序执行到这里证明特征到读取已经完成我们可以在回调中取出特征到值
                //特征所包含到值包含在一个byte数组内，我们可以定义一个临时的变量来获取

//                var characteristicBytes = characteristic?.value
                //如果这个特征返回的是字符串可以直接获取
//                var vlueStr = characteristicBytes?.let { String(it) }

                //如果只需取的其中的几个byte  可以直接指定获取特定的数组位置的byte值
                //例如协议当中定义了这串数据当中前两个byte表示特定的一个数值，那么获取这个值直接可以写成
//                        var avBytes = Byte[]{
//                            characteristic!!.value[0]
//                            characteristic!!.value[1]
//                        }
                characteristic?.let { broadcastUpdate(ACTION_DATA_AVAILABLE, it) }
            }
        }
    }

    //写入特征后回调这里
    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        super.onCharacteristicWrite(gatt, characteristic, status)
    }

    //当特征值发生变化时回调这里
    //当我们执行了gatt.setCharacteristicNotification或写入特征的时候结果回调在此
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
//        super.onCharacteristicChanged(gatt, characteristic)
        //当我们决定用通知当方式获取获取外设特征的时候，每当特征值发生变化。程序就会回调到此处
        //在一个gatt链接当中，可以同时存在多个notify的回调，全部值都会回调到这里，那么我们如何区分这些值的来源
        //这个时候，我们就需要去判断回调回来特征的uuid，因为uuid是唯一的，所以我们可以用uuid来确定这些数据来源那个特征
        //假设我们已经在BleServiece当中定来多个我们想要使用的uuid，前面已经说过如何表达一个uuid
        //那我们需要做的就是对比这些uuid根据不同的uuid来分类这些数据，到底应该交给那个处理

        //所以这么一来就会发现其实上面onCharacteristicRed也会出现这种情况
        //因为我们不可能只读取一个特征，除非这个外设也只有一个特征
        //究竟是水谁在读取，读取的值来源于那个特征，都需要进行判断
        characteristic?.let { broadcastUpdate(ACTION_DATA_AVAILABLE, it) }
    }

    //读取描述符后回调这里
    override fun onDescriptorRead(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        super.onDescriptorRead(gatt, descriptor, status)
    }

    //写入描述符后回调这里
    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
    }

    override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
        super.onReliableWriteCompleted(gatt, status)
    }

    //rssi信号强度发生变化时候调用这里
    override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
        super.onReadRemoteRssi(gatt, rssi, status)
    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        super.onMtuChanged(gatt, mtu, status)
    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        service.sendBroadcast(intent)
    }

    private fun broadcastUpdate(
        action: String,
        characteristic: BluetoothGattCharacteristic
    ) {
        val intent = Intent(action)

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            val flag = characteristic.properties
            var format = -1
            if (flag and 0x01 != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16
                LogUtils.i("Heart rate format UINT16.")
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8
                LogUtils.i("Heart rate format UINT8.")
            }
            val heartRate = characteristic.getIntValue(format, 1)
            LogUtils.i(String.format("Received heart rate: %d", heartRate))
            intent.putExtra(EXTRA_DATA, heartRate.toString())
        } else {
            // For all other profiles, writes the data formatted in HEX.
            val data = characteristic.value
            if (data != null && data.size > 0) {
                val stringBuilder = StringBuilder(data.size)
                for (byteChar in data) stringBuilder.append(
                    String.format(
                        "%02X ",
                        byteChar
                    )
                )
                intent.putExtra(EXTRA_DATA, """${String(data)}$stringBuilder""".trimIndent()
                )
            }
        }
        service.sendBroadcast(intent)
    }

}