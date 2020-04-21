package com.wyl.monitor.util;

import android.util.Log;

import java.util.Arrays;

public class SendDataStructure {

    byte top;
    int length;
    byte[] data;
    byte[] crc = new byte[2];

    public void setData(byte[] data) {
        byte[] newData = new byte[data.length + 2];
        newData[0] = top;
        newData[1] = (byte) data.length;
        this.data = newData;
        System.arraycopy(data,0,newData,2,data.length);
        int dataInt[] = new int[newData.length];
        for(int i = 0 ; i < newData.length;i++){
            dataInt[i] = Integer.parseInt(HexString.toHexOneString(newData[i]), 16);
        }
        crc = HexString.TowByteArray(CRC32Or16.getCrc(newData));
        length = data.length;
    }

    public void setTop(byte top) {
        this.top = top;
    }

    public byte[] getCmd(){
        byte[] cmd = new byte[ data.length + 4];//帧头 + 数据段 + 校验
        cmd[0] = (byte) 0xfe;
        cmd[1] = (byte) 0xef;
        System.arraycopy(data,0,cmd,2,data.length);
        System.arraycopy(crc,0,cmd,cmd.length - 2,crc.length);
        return cmd;
    }

   /* public static void  main(String[] ss){
        SendDataStructure sendData = new SendDataStructure();
        sendData.setData(new byte[]{-126,18,127,-85,-43});
    }*/
}
