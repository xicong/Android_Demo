package com.cong.demo.bluetooth

data  class BluetoothDeviceBean(
   val name:String,
   val address:String,
   val type:Int,
   val bondState:Int
)