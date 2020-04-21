package com.wyl.monitor.analysis;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.util.Log;

import com.wyl.monitor.analysis.CommandCallBack.OnCommandCallBack;
import com.wyl.monitor.analysis.OnUpgradeCallBack.OnUpgradeListener;
import com.wyl.monitor.analysis.ParameterCallBack.OnParameterBack;
import com.wyl.monitor.analysis.ParameterCallBack.Parameter;
import com.wyl.monitor.analysis.SyncCallBack.OnSyncCommandBack;
import com.wyl.monitor.analysis.VersionsCallBack.OnInquireVersionsListener;
import com.wyl.monitor.analysis.VersionsCallBack.VersionMessage;
import com.wyl.monitor.base.BaseDevice;
import com.wyl.monitor.entity.pneumatic_bed.RealTimeRedData;
import com.wyl.monitor.util.ArrayUtil;
import com.wyl.monitor.util.CRC32Or16;
import com.wyl.monitor.util.HexString;
import com.wyl.monitor.util.SendDataStructure;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/*
* ## 实时数据协议
开启后,设备每秒发送一条
| 设备上发报文 | 报文类型 | 数据段 |
| :------: | :------: | :------: |
| 实时数据 | 0x30 | [0]:当前心率<br> [1]:当前呼吸率<br> [2]:当前打鼾<br> [3]:当前体动<br> [4]:当前睡眠状态<br> [5]~[24]:心率波形<br> [25]~[44]:呼吸波形<br> [45]~[64]: 打鼾波形<br> |
*/

public class PillowDevice extends BaseDevice {

    private String TAG = PillowDevice.class.getName();

    @Override
    public UUID getServiceUUID() {
        return UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public UUID getNotifyUUID() {
        return UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public UUID getWriteUUID() {
        return UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public UUID getReadUUID() {
        return null;
    }


    public PillowDevice(Context context, BluetoothGatt bluetoothGatt) {
        super(context, bluetoothGatt);
        topTagList = Arrays.asList(topTAG);
    }

    //每条回调指令的数据长度
    private int dataLength[] = new int[]{
            17, -2, 10, 6, 8, 7, 14, 77, 14, 8, 6
    };
    //每条回调指令的头码
    private String topTAG[] = new String[]{
            "FEEFA0", "FEEFA2", "FEEFA3", "FEEF90", "FEEF91", "FEEF92", "FEEF95", "FEEFB0", "FEEFB1", "FEEFD1", "FEEFD2"
    };
    //"FEEF30","FEEFB1","FEEFA2"




    private List<String> topTagList;



    public static VersionMessage versionMessage = null;//固件版本信息
    public  volatile Parameter parameter = new Parameter(); //设备参数信息
    private OnCommandCallBack onCommandCallBack;



    public void setOnCommandCallBack(OnCommandCallBack onCommandCallBack) {
        this.onCommandCallBack = onCommandCallBack;
    }

    /**
     * 实时数据
     *
     * @param datas
     * @param characteristic
     */
    private final int INITIALIZATION_DATA = -1;//初始化数据
    private final int SYNCHRONOUS_DATA = -2;//同步数据
    private boolean isPackage = false;//数据是否拼包
    private int currentDataLength = INITIALIZATION_DATA;//当前解析的数据长度
    private byte[] temporaryDate = new byte[]{};//暂存数据
    private long overtime = 0;

    @Override
    public synchronized void CallbackData(byte[] datas, BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "设备回复" + HexString.toHexString(datas, 0, datas.length));
        if (temporaryDate != null || currentDataLength == SYNCHRONOUS_DATA)
            temporaryDate = ArrayUtil.addBytes(temporaryDate, datas);

        if (!isPackage && temporaryDate.length > 3) {
            String top = HexString.toHexString(temporaryDate, 0, temporaryDate.length).substring(0, 6);
            if (topTagList.contains(top)) {
                currentDataLength = dataLength[topTagList.indexOf(top)];
                if (currentDataLength == SYNCHRONOUS_DATA) {//如果是同步数据就不需要按长度截取（原因不知道数据有多长）
                    isPackage = true;
                    qequestMethodsResponse = true;
                } else {
                    isPackage = datas.length < currentDataLength;
                }
            }else{
                //temporaryDate = new byte[]{};
                //return;
            }
        }


        if (currentDataLength == SYNCHRONOUS_DATA) { //特殊数据不需要拼包

            if (temporaryDate == null) {
                temporaryDate = ArrayUtil.addBytes(temporaryDate, datas);
            }

            if (ValidationCheck(temporaryDate) && onSyncCommandBack != null) {
                analyzeData(temporaryDate);
                temporaryDate = new byte[]{};
                currentDataLength = INITIALIZATION_DATA;
                isPackage = false;
            }

        } else if ((temporaryDate.length >= currentDataLength &&
                currentDataLength != INITIALIZATION_DATA) ||
                ValidationCheck(temporaryDate)) {
            //对接收数据进行处理，有的数据长度大于20个字节，需要拼包
            byte[] commandData = new byte[currentDataLength];

            try {
                System.arraycopy(temporaryDate, 0, commandData, 0, currentDataLength);
            } catch (Exception e) {
                return;
            }
            if (temporaryDate.length - currentDataLength > 0) {
                byte[] surplusDate = new byte[temporaryDate.length - currentDataLength];//剩余数据
                System.arraycopy(temporaryDate, currentDataLength, surplusDate, 0, temporaryDate.length - currentDataLength);
                String head = surplusDate.length > 6 ? HexString.toHexString(surplusDate, 0, surplusDate.length).substring(0, 6) : "";

                if (topTagList.contains(head)) {
                    if (ValidationCheck(surplusDate)) {
                        temporaryDate = new byte[]{};
                        isPackage = false;
                    } else {
                        temporaryDate = surplusDate;
                    }

                } else {
                    isPackage = false;
                    temporaryDate = new byte[]{};
                }
            } else {
                isPackage = false;
                temporaryDate = new byte[]{};
                currentDataLength = INITIALIZATION_DATA;
            }
            analyzeData(commandData);
        } else {
            String head = temporaryDate.length > 6 ? HexString.toHexString(temporaryDate, 0, temporaryDate.length).substring(0, 6) : "";
            if (topTagList.contains(head)) {

            } else {
                temporaryDate = new byte[]{};
            }
        }
    }

    /**
     * 验证数据是否可用
     *
     * @param surplusDate
     * @return
     */
    private boolean ValidationCheck(byte[] surplusDate) {
        if (surplusDate.length < 6)
            return false;
        byte[] newData = new byte[surplusDate.length - 4];//去掉帧头长度和校验长度
        System.arraycopy(surplusDate, 2, newData, 0, newData.length);
        byte[] crc = HexString.TowByteArray(CRC32Or16.getCrc(newData));
        boolean b = (crc[0] == surplusDate[surplusDate.length - 2] && crc[1] == surplusDate[surplusDate.length - 1]);
        return b;
    }



    /**
     * 分析数据，调用对应的方法
     *
     * @param data
     */
    private void analyzeData(byte[] data) {

        String cmd = HexString.toHexString(data, 0, data.length);
        String top = cmd.substring(0, 6);
        currentDataLength = INITIALIZATION_DATA;
        isPackage = false;
        //  Log.d(TAG, Arrays.toString(data) + "\n" + HexString.toHexString(data, 0, data.length));

        switch (top) {
            case "FEEFA0"://版本号查询
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnVersionBack(data,onInquireVersionsListener);
                break;

            case "FEEFA2":
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnSynsDataBack(data,onSyncCommandBack);
                break;

            case "FEEFA3":
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnSynsDataCheckBack(data,onSyncCommandBack);
                break;

            case "FEEF90"://请求升级固件回调
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnUpgradeFirmwareBack(data,onUpgradeListener);
                break;

            case "FEEF91"://返回当前的进度包数
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnFirmwarePackageCount(data,onUpgradeListener);

                break;

            case "FEEF92"://固件升级结果
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnUpgradeFirmwareResultsBack(data,onUpgradeListener);

                break;

            case "FEEF95"://回复Bootloader版本

                break;

            case "FEEFB0"://实时数据
                if(onCommandCallBack!=null)
                    onCommandCallBack.OnRealTimeRedDataBack(data,getOnRealTimeDataListener());

                break;

            case "FEEFB1"://查询设备参数
               /* byte[] timeByte = new byte[]{data[4], data[5], data[6], data[7]};
                deviceTime = HexString.byteArrayToInt(timeByte);
                deviceTime = deviceTime * 1000;
                isOpenData = (data[8] & 0xff) == 1 ? true : false;
                isStatistics = (data[9] & 0xff) == 1 ? true : false;
                isElectric = (data[10] & 0xff) == 1 ? true : false;
                sideHeight = (data[11] & 0xff);
                if (!isOpenData && !isSynchronousData) {
                    sendSetParam(System.currentTimeMillis(), !isOpenData, isStatistics);
                }*/

                if(onCommandCallBack!=null)
                    parameter = onCommandCallBack.OnParameterBack(data,onParameterBack);
                break;
        }
    }


    /*-------------------------------------------------实时数据解析---------------------------------------------*/

    /**
     * 实时心率呼吸打鼾等数据解析
     *
     * @param data
     */
    private void AnalyzeRealTimeRedData(String cmd, byte[] data) {
        //FE EF B0 43 46 11 00 01 020023236032280A56442C26120F224731252A2B28231E1D1B1B1C1B1A1A1B1C1D1F2123252629292A2A21212222222125272422222248412D2523222222008231
        Log.e(TAG, "实时数据" + cmd);
        RealTimeRedData realTimeRedData = new RealTimeRedData();
        realTimeRedData.setCurrentHeartRate(data[4] & 0xff);//当前心率
        realTimeRedData.setCurrentRespiratoryRate(data[5] & 0xff);//当前呼吸率
        realTimeRedData.setSnore(data[6] & 0xff);//是否打鼾
        realTimeRedData.setMove(data[7] & 0xff);//是否体动
        realTimeRedData.setSleepStatus(data[8] & 0xff);//睡眠状态
        realTimeRedData.setTotalling(data[9] & 0xff); //是否在监测睡眠中
        realTimeRedData.setSleepingPosture(data[70] & 0xff); //睡姿(0-平躺,1-侧睡)
        realTimeRedData.setPositivePole(((data[71] & 0xff) << 8) | (data[72] & 0xff)); //静电正
        realTimeRedData.setCathode(((data[73] & 0xff) << 8) | (data[74] & 0xff));// 静电负
        int xlOrTd[] = new int[20];//心率波形
        int hx[] = new int[20];//呼吸波形
        int dh[] = new int[20];//打鼾波形
        for (int i = 0; i < 20; i++) {
  /*          xlOrTd[i] = realTimeRedData.isMove() ? (data[5 + i] & 0xff) + 1 :
                    Math.max(Math.min((data[5 + i] & 0xff) * 2 - 48, 98), 0);*/
            xlOrTd[i] = data[10 + i] & 0xff;
            int hxVa = (data[30 + i] & 0xff);
            hx[i] = hxVa > 2 ? hxVa / 2 : hxVa;

            dh[i] = data[50 + i] & 0xff;
        }

/*        if (realTimeRedData.isMove()) {
            realTimeRedData.setXl(new int[20]);
            realTimeRedData.setTd(xlOrTd);
        } else {
            realTimeRedData.setXl(xlOrTd);
            realTimeRedData.setTd(new int[20]);
        }*/
        realTimeRedData.setXl(xlOrTd);
        realTimeRedData.setHx(hx);
        realTimeRedData.setDh(dh);
        Log.e(TAG, "实时数据" + realTimeRedData.toString());
        //Log.d(TAG,realTimeRedData.toString());
        OnRealTimeData(realTimeRedData);
    }


    /**
     * 设置参数
     *
     * @param isOpenData 是否开启实时数据
     */
    public void sendSetParam(long date, boolean isOpenData, boolean isStatistics) {
        this.onParameterBack = onParameterBack;
        if (date == 0) {
            date = System.currentTimeMillis();
        }
        int time = (int) (date / 1000);
        SendDataStructure sendData = new SendDataStructure();
        sendData.setTop((byte) 0x31);
        byte[] data = new byte[8];// 时间戳,1 Byte实时开关(1开启实时数据)
        System.arraycopy(HexString.intToByteArray(time), 0, data, 0, 4);
        data[4] = (byte) (isOpenData ? 1 : 0);
        data[5] = (byte) (isStatistics ? 1 : 0);
        data[6] = (byte) (parameter.isElectric() ? 1 : 0);
        data[7] = (byte) (parameter.getSideHeight());
        sendData.setData(data);
        byte[] cmd = sendData.getCmd();
        sendData(cmd);
    }

    /**
     * 设置枕头高度
     *
     * @param date
     * @param sideHeight
     */
    public void sendSetParam(long date, int sideHeight) {
        if (date == 0) {
            date = System.currentTimeMillis();
        }
        int time = (int) (date / 1000);
        SendDataStructure sendData = new SendDataStructure();
        sendData.setTop((byte) 0x31);
        byte[] data = new byte[8];// 时间戳,1 Byte实时开关(1开启实时数据)
        System.arraycopy(HexString.intToByteArray(time), 0, data, 0, 4);
        data[4] = (byte) (parameter.isOpenData() ? 1 : 0);
        data[5] = (byte) (parameter.isStatistics() ? 1 : 0);
        data[6] = (byte) (parameter.isElectric() ? 1 : 0);
        data[7] = (byte) (sideHeight);
        sendData.setData(data);
        byte[] cmd = sendData.getCmd();
        sendData(cmd);
    }

    /**
     * 设置静电开关
     *
     * @param date
     * @param isElectric
     */
    public void sendSetParam(long date, boolean isElectric) {
        if (date == 0) {
            date = System.currentTimeMillis();
        }
        int time = (int) (date / 1000);
        SendDataStructure sendData = new SendDataStructure();
        sendData.setTop((byte) 0x31);
        byte[] data = new byte[8];// 时间戳,1 Byte实时开关(1开启实时数据)
        System.arraycopy(HexString.intToByteArray(time), 0, data, 0, 4);
        data[4] = (byte) (parameter.isOpenData() ? 1 : 0);
        data[5] = (byte) (parameter.isStatistics() ? 1 : 0);
        data[6] = (byte) (isElectric ? 1 : 0);
        data[7] = (byte) (parameter.getSideHeight());
        sendData.setData(data);
        byte[] cmd = sendData.getCmd();
        sendData(cmd);
    }

    //FE EF B1 06 00 00 06 14 00 00 E5 91
    /**
     * 设置参数
     *
     * @param isOpenData 是否开启实时数据
     */
    private OnParameterBack onParameterBack;
    public void sendSetParam(long date, boolean isOpenData, boolean isStatistics, OnParameterBack onParameterBack) {
        this.onParameterBack = onParameterBack;
        if (date == 0) {
            date = System.currentTimeMillis();
        }
        int time = (int) (date / 1000);
        SendDataStructure sendData = new SendDataStructure();
        sendData.setTop((byte) 0x31);
        byte[] data = new byte[8];// 时间戳,1 Byte实时开关(1开启实时数据)
        System.arraycopy(HexString.intToByteArray(time), 0, data, 0, 4);
        data[4] = (byte) (isOpenData ? 1 : 0);
        data[5] = (byte) (isStatistics ? 1 : 0);
        data[6] = (byte) (parameter.isElectric() ? 1 : 0);
        data[7] = (byte) (parameter.getSideHeight());
        sendData.setData(data);
        byte[] cmd = sendData.getCmd();
        sendData(cmd);
    }




    /**
     * 查询设备参数
     */
    public void sendInquire() {
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x32);
        sendDataStructure.setData(new byte[0]);
        byte[] cmd = sendDataStructure.getCmd();
        System.out.println(Arrays.toString(cmd));
        sendData(cmd);
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!parameter.isOpenData()) {
                    sendInquire();
                }
            }
        }).start();*/
    }


    /**
     * 查询设备参数
     */
    public void sendInquire(OnParameterBack onParameterBack) {
        this.onParameterBack = onParameterBack;
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x32);
        sendDataStructure.setData(new byte[0]);
        byte[] cmd = sendDataStructure.getCmd();
        System.out.println(Arrays.toString(cmd));
        sendData(cmd);
  /*      new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!parameter.isOpenData()) {
                    sendInquire();
                }
            }
        }).start();*/
    }

    /*-------------------------------------------------数据同步指令---------------------------------------------*/

    private OnSyncCommandBack onSyncCommandBack;

    /**
     * 发送请求同步数据
     */
    private boolean qequestMethodsResponse = false; //发送请求同步数据是否响应

    public void sendQequestSynchronousData(long time, final OnSyncCommandBack onSyncCommandBack) {
        this.onSyncCommandBack = onSyncCommandBack;
        onSyncCommandBack.setPillowDevice(this); //传入设备对象
        qequestMethodsResponse = false;
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x21);
        if (time != 0) {
            time /= 1000;
        }
        byte[] data = HexString.FourByteArray((int) time);
        sendDataStructure.setData(data);
        byte[] cmd = sendDataStructure.getCmd();
        sendData(cmd);
        //fe ef 21 04 00 00 00 00 f9 ae
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!qequestMethodsResponse) {
                    //sendInquire();
                    onSyncCommandBack.OnSyncOvertime();
                }
            }
        }).start();
    }


    /**
     * 发送同步数据包
     *
     * @param count     当前的包数
     * @param isSuccess 数据是否真确
     */
    public boolean sendPackageSynchronousData(int count, boolean isSuccess, OnSyncCommandBack onSyncCommandBack) {
        this.onSyncCommandBack = onSyncCommandBack;
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x22);
        byte[] data = new byte[3];
        System.arraycopy(HexString.TowByteArray(count), 0, data, 0, 2);
        data[2] = (byte) (isSuccess ? 1 : 0);
        sendDataStructure.setData(data);
        byte[] cmd = sendDataStructure.getCmd();
        return sendData(cmd);
    }


    /**
     * 发送同步结束
     *
     * @param isSuccess //1 Byte结果(0:失败; 1:成功)
     */
    public void sendFinishSynchronousData(boolean isSuccess, OnSyncCommandBack onSyncCommandBack) {
        if (onSyncCommandBack != null)
            this.onSyncCommandBack = onSyncCommandBack;
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x23);
        sendDataStructure.setData(new byte[]{(byte) (isSuccess ? 1 : 0)});
        byte cmd[] = sendDataStructure.getCmd();
        sendData(cmd);
    }


    /*-------------------------------------------------固件升级协议---------------------------------------------*/
    private OnUpgradeListener onUpgradeListener;
    public void setOnUpgradeListener(OnUpgradeListener onUpgradeListener) {
        this.onUpgradeListener = onUpgradeListener;
    }


    /**
     * 固件升级请求  0x10
     */
    public void sendQequestFirmwareUpgrade(OnUpgradeListener onUpgradeListener) {
        this.onUpgradeListener = onUpgradeListener;
        byte[] crc = HexString.TowByteArray(CRC32Or16.CalcCRC16(new int[]{0x10, 0x00}));
        byte[] bytes = new byte[]{(byte) 0xFE, (byte) 0xEF, 0x10, 0x00, crc[0], crc[1]};
        sendData(bytes);
    }


    /**
     * Bin文件数据 0x11
     * @param count    计包数
     * @param fileData 文件数据
     */
    //private byte[] zonFile = new byte[0];
    public void sendFileData(int count, byte[] fileData, OnUpgradeListener onUpgradeListener) {
        this.onUpgradeListener = onUpgradeListener;
        //  zonFile = new byte[zonFile.length + fileData.length];
        //  System.arraycopy(fileData, 0, zonFile, zonFile.length - fileData.length, fileData.length);

        byte[] countByte = HexString.TowByteArray(count);
        byte[] data = new byte[countByte.length + fileData.length];
        System.arraycopy(countByte, 0, data, 0, countByte.length);
        System.arraycopy(fileData, 0, data, countByte.length, fileData.length);
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x11);
        sendDataStructure.setData(data);
        final byte[] cmd = sendDataStructure.getCmd();
        final int sum = cmd.length / 20;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < sum; i++) {
                    byte[] newCmd = new byte[20];
                    System.arraycopy(cmd, i * 20, newCmd, 0, newCmd.length);
                    // Log.e(TAG, "分包：" + Arrays.toString(newCmd));
                    sendData(newCmd);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (cmd.length % 20 != 0) {
                    byte[] newCmd = new byte[cmd.length % 20];
                    System.arraycopy(cmd, (sum) * 20, newCmd, 0, newCmd.length);
                    //  Log.e(TAG, "分包：" + Arrays.toString(newCmd));
                    sendData(newCmd);
                }
            }
        }).start();

    }


    /**
     * Bin文件校验 0x12
     */
    public void sendFileCheckout(byte[] fileData, OnUpgradeListener onUpgradeListener) {
        this.onUpgradeListener = onUpgradeListener;

        int[] newFileData = new int[fileData.length];
        for (int i = 0; i < fileData.length; i++) {
            newFileData[i] = Integer.parseInt(HexString.toHexOneString(fileData[i]), 16);
        }

        byte[] crc32 = HexString.FourByteArray((int) CRC32Or16.CalcCRC32(fileData));
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x12);
        sendDataStructure.setData(crc32);
        byte[] cmd = sendDataStructure.getCmd();
        Log.e(TAG, Arrays.toString(cmd));
        sendData(cmd);
    }


    private OnInquireVersionsListener onInquireVersionsListener;


    /**
     * 查询设备版本号 0x20
     */
    public void sendInquireVersions(OnInquireVersionsListener onInquireVersionsListener) {
        this.onInquireVersionsListener = onInquireVersionsListener;
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x20);
        sendDataStructure.setData(new byte[0]);
        sendData(sendDataStructure.getCmd());
    }


    /**
     * 清楚设备数据
     */
    public void sendClearContents() {
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x52);
        sendDataStructure.setData(new byte[0]);
        sendData(sendDataStructure.getCmd());
    }


    /**
     * 生成数据
     */
    public void sendCreate(long time) {
        SendDataStructure sendDataStructure = new SendDataStructure();
        sendDataStructure.setTop((byte) 0x51);
        sendDataStructure.setData(new byte[]{(byte) ((time >> 8) & 0xFF), (byte) (time & 0xFF)});
        sendData(sendDataStructure.getCmd());
    }

}



