package com.wyl.monitor.analysis;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.wyl.monitor.base.BaseDevice;
import com.wyl.monitor.database.DailyTotalDataBaseUtil;
import com.wyl.monitor.database.RecordsSleepDataBaseUtil;
import com.wyl.monitor.entity.pneumatic_bed.DailyTotalData;
import com.wyl.monitor.entity.pneumatic_bed.RealTimeRedData;
import com.wyl.monitor.entity.pneumatic_bed.RecordsSleep;
import com.wyl.monitor.util.ArrayUtil;
import com.wyl.monitor.util.HexString;
import com.wyl.monitor.util.ScoreUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class PillowDevice1 extends BaseDevice {

    private String TAG = PillowDevice1.class.getName();
    private int format = BluetoothGattCharacteristic.FORMAT_UINT8;
    public HandlerThread handlerThread;
    private Handler handler;

    @Override
    public UUID getServiceUUID() {
        return UUID.fromString("0000ffb0-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public UUID getNotifyUUID() {
        return UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public UUID getWriteUUID() {
        return UUID.fromString ("0000ffb1-0000-1000-8000-00805f9b34fb");
    }


    @Override
    public UUID getReadUUID() {
        return null;
    }


    public PillowDevice1(Context context, BluetoothGatt bluetoothGatt) {
        super(context, bluetoothGatt);
        handlerThread = new HandlerThread("aa");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        OpenOrCloseData(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < byteDataList.size(); i++) {
                                    StatisticalData(byteDataList.get(i));
                                }
                                for (DailyTotalData dailyTotalData : dailyTotalDataList) {
                                    boolean a = DailyTotalDataBaseUtil.deleteDailyTotalDatas(dailyTotalData.getStartTime(),dailyTotalData.getEndTime());
                                    boolean b = RecordsSleepDataBaseUtil.deleteAppointTime(dailyTotalData.getStartTime(),dailyTotalData.getEndTime());
                                    //  Log.d(TAG,"DailyTotalData: " + (a ? "删除成功": "删除失败"));
                                    //   Log.d(TAG,"DailyTotalData: " + (b ? "删除成功": "删除失败"));
                                    dailyTotalData.save();
                                }
                                RecordsSleepDataBaseUtil.saveAll(recordsSleeps);
                                Log.d(TAG,"RecordsSleep: " + LitePal.findAll(RecordsSleep.class).size());
                                byteDataList.clear();
                                dailyTotalDataList.clear();
                                recordsSleeps.clear();
                            }
                        }).start();
                        break;
                }
            }
        };
    }


    /**
     * 开启或关闭实时数据
     */
    public void OpenOrCloseData(boolean isOpen) {
        int time = (int) (System.currentTimeMillis() / 1000);
        //int ECC  = getECC(new int[]{0x53,0x42,0x32,0x42,0x32,time,});
        String cmd = "31" +                                         //头码
                HexString.hexToSix(time, 8)+               //时间戳
                HexString.hexToSix(isOpen ? 1 : 0,2);
        byte[] bytes = HexString.hexTobytes(cmd);
        sendData(bytes);
    }

    /**
     * 查询设备参数
     */
    public void Inquire(){
        String cmd = "b1";
        byte[] bytes = HexString.hexTobytes(cmd);
        sendData(bytes);
    }


    private byte[] bytes = new byte[0];
    private int currentSpliceLength;//当前拼接长度
    private byte[] redundantData;//多余的数据
    private String currentDataFlag;
    private boolean isJoint = false;//是否拼接数据
    private int bytesPosition = 0; //标识当前存储位置

    @Override
    public void CallbackData(byte[] datas, BluetoothGattCharacteristic characteristic){
        Log.d(TAG, "-----" + Arrays.toString(datas));
        if (redundantData != null) {
            datas = ArrayUtil.addBytes(redundantData, datas);
            Log.d(TAG, "多余数据拼接后-----" + Arrays.toString(datas));
            redundantData = null;
        }
        int top = datas[0] & 0xff;
        if (!isJoint)
            switch (top) {
                case 0x8A:
                    bytes = new byte[16];
                    currentDataFlag = "8A";
                    isJoint = true;
                    break;
                case 0x8E:
                    bytes = new byte[82];
                    currentDataFlag = "8E";
                    isJoint = true;
                    break;
                case 0x8D:
                    bytes = new byte[92];
                    currentDataFlag = "8D";
                    isJoint = true;
                    break;
                case 0x8C:
                    bytes = new byte[93];
                    isJoint = true;
                    break;
                case 0x9D:
                    bytes = new byte[16];
                    currentDataFlag = "9D";
                    isJoint = true;
                    break;
            }
        currentSpliceLength = bytes.length;

        if (isJoint) {
            //分包处理数据
            int dataLength = bytesPosition + datas.length;
            if (dataLength > currentSpliceLength) {
                System.arraycopy(datas, 0, bytes, bytesPosition, Math.abs(datas.length - (dataLength - currentSpliceLength)));
                int head = (datas[Math.abs(datas.length - (dataLength - currentSpliceLength))] & 0xff);//判断多余数据是否是头码
                int redundantLength = dataLength - currentSpliceLength;//多余的数据长度
                if (head == 0x8A || head == 0x8E || head == 0x8D || head == 0x8C || head == 0x9D || head == 0x8A) {
                    redundantData = new byte[redundantLength];
                    System.arraycopy(datas, Math.abs(datas.length - (dataLength - currentSpliceLength)), redundantData, 0, redundantLength);
                    Log.d(TAG, "多余的" + Arrays.toString(redundantData));
                    isJoint = false;
                    bytesPosition = 0;
                    currentSpliceLength = 0;
                } else {
                    byte[] lan = new byte[redundantLength];
                    System.arraycopy(datas, Math.abs(datas.length - (dataLength - currentSpliceLength)), lan, 0, redundantLength);
                    Log.d(TAG, head + "烂数据" + Arrays.toString(lan));
                    isJoint = false;
                    bytesPosition = 0;
                    currentSpliceLength = 0;
                }
            } else {
                System.arraycopy(datas, 0, bytes, bytesPosition, datas.length);
                bytesPosition = bytesPosition + datas.length;
            }
        } else {


        }
        String cmd = HexString.toHexString(bytes, 0, bytes.length);
        if (bytesPosition == currentSpliceLength) {
            //Log.e(TAG,"currentSpliceLength = " +currentSpliceLength+ " bytesPosition = "+bytesPosition +" cmd "+ cmd);
            bytesPosition = 0;
            currentSpliceLength = 0;
            subPackageOnce(cmd.substring(0, 2), cmd, bytes);
            isJoint = false;
        }
    }


    //数据解析
    public void subPackageOnce(String top, String cmd, byte[] data) {

        Log.d("解析", "cmd = " + cmd);
        switch (top) {
            case "8A":

                break;

            case "8E":

                break;

            case "8D":
                handler.removeCallbacksAndMessages(null);
                lastLongFlags = 0;
                byteDataList.add(data);
                handler.sendEmptyMessageDelayed(1, 2000);
                break;

            case "8C":
                    AnalyzeRealTimeRedData(cmd, data);
                break;
            case "9D":
                if (data[12] == 1) {
                    OpenOrCloseData(false);
                }
                break;

            default:
                Log.e(TAG, "有问题" + cmd);
                break;
        }
    }

    /**
     * 解析接收实时数据
     *
     * @param cmd
     * @param data
     */
    private void AnalyzeRealTimeRedData(String cmd, byte[] data) {
        RealTimeRedData realTimeRedData = new RealTimeRedData();
        realTimeRedData.setReStartTime(Integer.parseInt(cmd.substring(10, 12), 16));
        realTimeRedData.setDirection(Integer.parseInt(cmd.substring(12, 14), 16));
        realTimeRedData.setPosition(Integer.parseInt(cmd.substring(14, 16), 16));
        realTimeRedData.setStrength(Integer.parseInt(cmd.substring(16, 18), 16));
        realTimeRedData.setSettime(((data[9] << 8) & 0xff00) + (data[10] & 0xff));
        realTimeRedData.setInStopping(Integer.parseInt(cmd.substring(22, 24), 16));
        realTimeRedData.setSleepStatus(Integer.parseInt(cmd.substring(24, 26), 16));
        realTimeRedData.setAutoMode(Integer.parseInt(cmd.substring(26, 28), 16));
        realTimeRedData.setTimeCounter(((data[14] << 8) & 0xff00) + (data[15] & 0xff));
        realTimeRedData.setTotalling(Integer.parseInt(cmd.substring(32, 34), 16));
        realTimeRedData.setCurrentHeartRate(Integer.parseInt(cmd.substring(34, 36), 16));
        realTimeRedData.setCurrentRespiratoryRate(Integer.parseInt(cmd.substring(36, 38), 16));
        realTimeRedData.setSnore(Integer.parseInt(cmd.substring(38, 40), 16));
        realTimeRedData.setMove(Integer.parseInt(cmd.substring(40, 42), 16));
        realTimeRedData.setIsSecMove(Integer.parseInt(cmd.substring(44, 46), 16));

        int xlOrTd[] = new int[20];//心率和体动值
        int hx[] = new int[20];//呼吸值
        int dh[] = new int[20];//呼吸值
        for (int i = 0; i < 20; i++) {
            xlOrTd[i] = realTimeRedData.isMove() ? (data[23 + i] & 0xff) + 1 :
                    Math.max(Math.min((data[23 + i] & 0xff) * 2 - 48, 98), 0);
            hx[i] = data[43 + i] & 0xff;
            dh[i] = data[63 + i] & 0xff;
        }

        if (realTimeRedData.isMove()) {
            realTimeRedData.setXl(new int[20]);
            realTimeRedData.setTd(xlOrTd);
        } else {
            realTimeRedData.setXl(xlOrTd);
            realTimeRedData.setTd(new int[20]);
        }
        realTimeRedData.setHx(hx);
        realTimeRedData.setDh(dh);

        int[] intensity = new int[7];
        for (int i = 0; i < 7; i++) {
            intensity[i] = data[83 + i];
        }
        realTimeRedData.setIntensity(intensity);
        // Log.d(TAG,realTimeRedData.toString());
        OnRealTimeData(realTimeRedData);
    }


    private int recordsSleepSum; // 睡眠记录总条数
    private int recordsSleepCount;//计数
    private List<byte[]> byteDataList = new ArrayList<>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     * 接收统计数据
     *
     * @param data
     */
    private long lastLongFlags;//记录，看时间是否一样，如果一样视为一个数据包
    private long currentLongTime;
    private int count = -1;
    private void StatisticalData(byte[] data) {
        String cmd = HexString.toHexString(data, 0, data.length);
        recordsSleepSum = Integer.parseInt(cmd.substring(12, 14), 16);
        recordsSleepCount = Integer.parseInt(cmd.substring(14, 16), 16);
        currentLongTime = Long.parseLong(cmd.substring(16, 24), 16) * 1000L;
        if(lastLongFlags == currentLongTime){
            currentLongTime = lastLongFlags +  ((1000 * 60 * 30) * count);
            count++;
        }else{
            if(lastLongFlags!=0)
                StatisticsSleepRecord(null, 0);//累计计算
            count = 1;
            lastLongFlags = currentLongTime;
        }
        Log.i("Totalling", "======================= " + simpleDateFormat.format(currentLongTime) + " ==========================" + currentLongTime);
        for (int i = 0; i < 30; i++) {
            int isSnoring = (data[(i * 2) + 24 + 5] >> 7) & (0x01);//TODO 打鼾标识
            int heartRate = (data[(i * 2) + 24 + 5] & (0x7F));      //TODO 心率
            int sleepStatus = (data[(i * 2) + 25 + 5] >> 7) & (0x01);//TODO 睡眠状态标识
            int isMove = (data[(i * 2) + 25 + 5] >> 6) & (0x01);//TODO 体动标识
            int respiratoryRate = (data[(i * 2) + 25 + 5] & (0x3F));//TODO 呼吸数值

            if (respiratoryRate != 0) {
                respiratoryRate += 2;
            }
            int sleep_Status;
            if ((i % 2) == 0) {
                sleep_Status = ((data[(i / 2) + 14] >> 4) & (0x07));
            } else {
                sleep_Status = (data[(i / 2) + 14] & (0x07));
            }
            if (respiratoryRate != 0 || heartRate != 0 || isSnoring != 0 || sleep_Status != 0 || isMove != 0) {
                RecordsSleep recordsSleep = new RecordsSleep();
                recordsSleep.setMac(getMac());
                recordsSleep.setElectricQuantity(Integer.parseInt(cmd.substring(24, 26), 16));
                recordsSleep.setSleepState(sleep_Status);
                recordsSleep.setSnoring(isSnoring);
                recordsSleep.setHeartRate(heartRate);
                recordsSleep.setMove(isMove);
                recordsSleep.setTime(simpleDateFormat.format(currentLongTime));
                Log.e(TAG, recordsSleep.toString());
                StatisticsSleepRecord(recordsSleep, (i) * 1000 * 60);//累计计算
            }
            currentLongTime += (1000 * 60);
        }

    }

    private List<DailyTotalData> dailyTotalDataList = new ArrayList<>();//睡眠记录（时间段区分）
    private DailyTotalData dailyTotalData;
    private long intervalTimeLong;
    private String startTime;//入睡开始时间
    private String endTime;//入睡结束时间
    private int sleepTime;//入睡时长
    private int avgHeartRate;//平均心率
    private int maxHeartRate;//最高心率
    private int minHeartRate;//最低心率
    private int avgBreathe;//平均呼吸
    private int maxBreathe;//最高呼吸
    private int minBreathe;//最低呼吸
    private int deepSleepTime;//深睡时长
    private int lightSleepTime;//浅睡时长
    private int soberTime;//清醒时长
    private int snoreTime;//打鼾时长
    private int moveNumber;//体动次数
    private List<RecordsSleep> recordsSleeps = new ArrayList<>();

    /**
     * 统计每次睡眠数据
     *
     * @param recordsSleep
     */
    private void StatisticsSleepRecord(RecordsSleep recordsSleep,long time) {
        if (recordsSleep!=null && currentLongTime == lastLongFlags||currentLongTime - lastLongFlags == time + (count == 1 ? 0 : (count-1) * ((1000 * 60 * 30)))) {
            startTime = simpleDateFormat.format(lastLongFlags);
            endTime = recordsSleep.getTime();
            sleepTime += 1;
            avgHeartRate += recordsSleep.getHeartRate();
            maxHeartRate = Math.max(maxHeartRate,recordsSleep.getHeartRate());
            minHeartRate = (minHeartRate == 0) ? recordsSleep.getHeartRate() : Math.min(minHeartRate,recordsSleep.getHeartRate());
            avgBreathe += recordsSleep.getRespiratoryRate();
            maxBreathe = Math.max(maxBreathe,recordsSleep.getRespiratoryRate());
            minBreathe = (minBreathe == 0) ? recordsSleep.getRespiratoryRate() : Math.min(minHeartRate,recordsSleep.getRespiratoryRate());
            switch (recordsSleep.getSleepState()){//睡眠状态(0:离床,2:清醒,3:浅睡,4:深睡)
                case 2:soberTime+=1;break;
                case 3:lightSleepTime+=1;break;
                case 4:deepSleepTime+=1;break;
            }
            snoreTime += recordsSleep.isSnoring() ? 1 : 0;
            moveNumber += recordsSleep.isMove() ? 1 : 0;
            recordsSleeps.add(recordsSleep);
        } else {
            if(sleepTime == 0)
                return;
            dailyTotalData = new DailyTotalData();
            dailyTotalData.setStartTime(startTime);
            dailyTotalData.setEndTime(endTime);
            dailyTotalData.setSleepTime(sleepTime);
            dailyTotalData.setAvgHeartRate(avgHeartRate/sleepTime);
            dailyTotalData.setMaxHeartRate(maxHeartRate);
            dailyTotalData.setMinBreathe(minHeartRate);
            dailyTotalData.setAvgBreathe(avgBreathe/sleepTime);
            dailyTotalData.setMaxBreathe(maxBreathe);
            dailyTotalData.setMinBreathe(minBreathe);
            dailyTotalData.setDeepSleepTime(deepSleepTime);
            dailyTotalData.setLightSleepTime(lightSleepTime);
            dailyTotalData.setSoberTime(soberTime);
            dailyTotalData.setSnoreTime(snoreTime);
            dailyTotalData.setMoveNumber(moveNumber);


            int zsmsc = dailyTotalData.getSleepTime();
            int sssc = dailyTotalData.getDeepSleepTime();//深睡
            int qssc = dailyTotalData.getLightSleepTime()*97/100;//浅睡
            int smxl = ((sssc + qssc)*100/zsmsc);
            int rs = sssc + qssc;
            int td = dailyTotalData.getMoveNumber();
            int s = ScoreUtil.countScore(zsmsc/60, rs, smxl, td);
            dailyTotalData.setTotalScore(s);
            dailyTotalDataList.add(dailyTotalData);
            ResetData();
        }
    }

    /**
     * 重置累计数据
     */
    private void ResetData(){
        dailyTotalData = null;
        intervalTimeLong = 0;
        startTime = "";//入睡开始时间
        endTime = "";//入睡结束时间
        sleepTime = 0;//入睡时长
        avgHeartRate = 0;//平均心率
        maxHeartRate = 0;//最高心率
        minHeartRate = 0;//最低心率
        avgBreathe = 0;//平均呼吸
        maxBreathe = 0;//最高呼吸
        minBreathe = 0;//最低呼吸
        deepSleepTime = 0;//深睡时长
        lightSleepTime = 0;//浅睡时长
        soberTime = 0;//清醒时长
        snoreTime = 0;//打鼾时长
        moveNumber = 0;//体动次数
    }

    /**
     * 发送开始或结束统计数据
     */
    public void sendStartStatistics(boolean isTotall, int getData) {
        int time = (int) (System.currentTimeMillis() / 1000);
        //int ECC  = getECC(new int[]{0x53,0x42,0x32,0x42,0x32,time,});
        String cmd = "9D" +
                HexString.hexToSix(14, 2) +                //LEN
                "5342324232" +                                          //ID
                HexString.hexToSix(time, 8) +                 //时间戳
                HexString.hexToSix(isTotall ? 1 : 0, 2) +    //统计
                HexString.hexToSix(getData, 2) +           //ACD
                "FF" +                                            //校验
                "0D0A";                                         //结束标识
        sendData(HexString.hexTobytes(cmd));
    }

    /**
     * 获取校验
     *
     * @param data
     * @return
     */
    private int getECC(int[] data) {
        int sun = 0;
        for (int i = 0; i < data.length; i++) {
            sun += data[i];
        }
        int avg = sun / data.length;
        return avg;
    }
}

