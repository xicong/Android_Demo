package com.wyl.monitor.analysis.SyncCallBack;

import android.util.Log;

import com.wyl.monitor.analysis.PillowDevice;
import com.wyl.monitor.database.DailyTotalDataBaseUtil;
import com.wyl.monitor.database.RecordsSleepDataBaseUtil;
import com.wyl.monitor.entity.pneumatic_bed.DailyTotalData;
import com.wyl.monitor.entity.pneumatic_bed.RecordsSleep;
import com.wyl.monitor.util.ArrayUtil;
import com.wyl.monitor.util.CRC32Or16;
import com.wyl.monitor.util.HexString;
import com.wyl.monitor.util.ScoreUtil;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * 同步指令处理
 */
public abstract class OnSyncCommandBack implements OnSyncCommandListener {

    private final String TAG = OnSyncCommandBack.class.getName();

    private boolean SyncOvertime = false;
    public String mac = "";
    private PillowDevice pillowDevice;
    private byte[] syncData = new byte[]{};

    private List<byte[]> byteDataList = new ArrayList<>();
    private List<RecordsSleep> recordsSleeps = new ArrayList<>();
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

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");



    public void setPillowDevice(PillowDevice pillowDevice) {
        this.pillowDevice = pillowDevice;
        this.mac = pillowDevice.getMac();
    }

    @Override
    public void OnContinueTransmit(byte[] data) {
     /*   //最后数据校验用到，把所有数据暂时存储
        byte[] newSync = new byte[syncData.length + (data.length - 6)];
        System.arraycopy(data, 4, newSync, syncData.length, data.length - 6);*/

        //最后数据校验用到，把所有数据暂时存储
        byte[] newSync = new byte[(data.length - 8)];
        System.arraycopy(data, 6, newSync, 0, newSync.length);

        syncData = ArrayUtil.addBytes(syncData, newSync);
        //Log.e("校验数据", Arrays.toString(syncData));
        byteDataList.add(data);
        this.mac = mac;
        int count = ((data[5] & 0xFF) | ((data[4] & 0xFF) << 8));

        Log.d(TAG, Arrays.toString(data) + "\n" + HexString.toHexString(data, 0, data.length));
        OnSyncNextData(count, pillowDevice);
    }




    /**
     * 解析每条睡眠记录
     *
     * @param data
     */
    Calendar calendar = Calendar.getInstance();
    private long timeDuration = -1;
    private long time;
    private int eachTteration = 5;//每次递加的值
    private int count = 1;//累加记录条数

    private void  AnalysisEachData(byte[] data) {
        byte[] newData;
        if (timeDuration < 0) {
            time = HexString.byteArrayToInt(new byte[]{data[6], data[7], data[8], data[9]});
            time = time * 1000;
            timeDuration = ((data[10] & 0xFF) << 8) | (data[11] & 0xFF);//持续时间
            count = 0;
            newData = new byte[data.length - 14];
            System.arraycopy(data, 12, newData, 0, newData.length);
        } else {
            newData = new byte[data.length - 8];
            System.arraycopy(data, 6, newData, 0, newData.length);
        }

        for (int i = 0; i < (newData.length / 5); i++) {
            int heartRate = newData[i * eachTteration] & 0xFF;// 心率
            int respiratoryRate = newData[1 + (i * eachTteration)] & 0xFF;//呼吸率
            int isMove = newData[2 + (i * eachTteration)] & 0xFF;//是否体动
            int isSnoring = newData[3 + (i * eachTteration)] & 0xFF;//是否打鼾
            int sleepState = newData[4 + (i * eachTteration)] & 0xFF;//睡眠状态(0:离床,2:清醒,3:浅睡,4:深睡)

            try {
                calendar.setTime(simpleDateFormat.parse(simpleDateFormat.format(time + (count * 1000 * 60))));
               /* calendar.set(Calendar.YEAR, 2020);
                calendar.set(Calendar.MONTH, 2);
                calendar.set(Calendar.DATE, 8);*/
            } catch (ParseException e) {
                e.printStackTrace();
            }
            RecordsSleep recordsSleep = new RecordsSleep();
            recordsSleep.setTime(simpleDateFormat.format(calendar.getTime()));
            recordsSleep.setHeartRate(heartRate);
            recordsSleep.setRespiratoryRate(respiratoryRate);
            recordsSleep.setMove(isMove);
            recordsSleep.setSnoring(isSnoring);
            Random random = new Random();
            int a = random.nextInt(5);
            a = a < 2 ? 4 : a;
            recordsSleep.setSleepState(a);
            recordsSleep.setMac(mac);
            Log.e(TAG, recordsSleep.toString());
            timeDuration = timeDuration - 1;
            count++;
            Log.e("","计时" + timeDuration);
            if(timeDuration <= 0){
                StatisticsSleepRecord(recordsSleep);
                StatisticsSleepRecord(null);
                timeDuration = -1;
                count = 1;
            }else{
                StatisticsSleepRecord(recordsSleep);
            }

        }

    }

    /**
     * 统计每次睡眠数据
     *
     * @param recordsSleep
     */
    private String startStrTime;//入睡开始时间

    private void StatisticsSleepRecord(RecordsSleep recordsSleep) {

        if(startStrTime == null)
            startStrTime = recordsSleep.getTime();

        if (recordsSleep == null){
            addDailyTotalData();
            startStrTime = null;
            return;
        }

        startTime = startStrTime;
        endTime = recordsSleep.getTime();
        sleepTime += 1;
        avgHeartRate += recordsSleep.getHeartRate();
        maxHeartRate = Math.max(maxHeartRate, recordsSleep.getHeartRate());
        minHeartRate = (minHeartRate == 0) ? recordsSleep.getHeartRate() : Math.min(minHeartRate, recordsSleep.getHeartRate());
        avgBreathe += recordsSleep.getRespiratoryRate();
        maxBreathe = Math.max(maxBreathe, recordsSleep.getRespiratoryRate());
        minBreathe = (minBreathe == 0) ? recordsSleep.getRespiratoryRate() : Math.min(minHeartRate, recordsSleep.getRespiratoryRate());

        switch (recordsSleep.getSleepState()) {//睡眠状态(0:离床,2:清醒,3:浅睡,4:深睡)
            case 2:
                soberTime += 1;
                break;
            case 3:
                lightSleepTime += 1;
                break;
            case 4:
                deepSleepTime += 1;
                break;
        }
        snoreTime += recordsSleep.isSnoring() ? 1 : 0;
        moveNumber += recordsSleep.isMove() ? 1 : 0;
        recordsSleeps.add(recordsSleep);
    }

    /**
     * 添加睡觉时间段
     */
    private void addDailyTotalData() {
        if (sleepTime == 0)
            return;
        dailyTotalData = new DailyTotalData();
        dailyTotalData.setStartTime(startTime);
        dailyTotalData.setEndTime(endTime);
        dailyTotalData.setSleepTime(sleepTime);
        dailyTotalData.setAvgHeartRate(avgHeartRate / sleepTime);
        dailyTotalData.setMaxHeartRate(maxHeartRate);
        dailyTotalData.setMinBreathe(minHeartRate);
        dailyTotalData.setAvgBreathe(avgBreathe / sleepTime);
        dailyTotalData.setMaxBreathe(maxBreathe);
        dailyTotalData.setMinBreathe(minBreathe);
        dailyTotalData.setDeepSleepTime(deepSleepTime);
        dailyTotalData.setLightSleepTime(lightSleepTime);
        dailyTotalData.setSoberTime(soberTime);
        dailyTotalData.setSnoreTime(snoreTime);
        dailyTotalData.setMoveNumber(moveNumber);
        dailyTotalData.setMac(mac);

        int zsmsc = dailyTotalData.getSleepTime();
        int sssc = dailyTotalData.getDeepSleepTime();//深睡
        int qssc = dailyTotalData.getLightSleepTime() * 97 / 100;//浅睡
        int smxl = ((sssc + qssc) * 100 / zsmsc);
        int rs = sssc + qssc;
        int td = dailyTotalData.getMoveNumber();
        int s = ScoreUtil.countScore(zsmsc / 60, rs, smxl, td);
        dailyTotalData.setTotalScore(s);
        dailyTotalDataList.add(dailyTotalData);
        ResetData();
    }

    /**
     * 重置累计数据
     */
    private void ResetData() {
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
     * 校验数据有没有错误
     *
     * @param crc32
     */
    @Override
    public void OnCheckout(int crc32) {
        int crc = (int) CRC32Or16.CalcCRC32(syncData);
        final boolean code = crc == crc32;
        pillowDevice.sendFinishSynchronousData(code, null);
        if (code) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Integer> indexs = new ArrayList<>();
                    for (int i = 0; i < byteDataList.size(); i++) {
                        // Log.e(TAG,Arrays.toString(byteDataList.get(i)));
                       AnalysisEachData(byteDataList.get(i));

                    }

                    for (DailyTotalData dailyTotalData : dailyTotalDataList) {
                        boolean a = DailyTotalDataBaseUtil.deleteDailyTotalDatas(dailyTotalData.getStartTime(), dailyTotalData.getEndTime());
                        boolean b = RecordsSleepDataBaseUtil.deleteAppointTime(dailyTotalData.getStartTime(), dailyTotalData.getEndTime());

                        Log.d(TAG, "DailyTotalData: " + (a ? "删除成功" : "删除失败"));
                        Log.d(TAG, "RecordsSleepData: " + (b ? "删除成功" : "删除失败"));
                        boolean c = dailyTotalData.save();
                        Log.d(TAG, "DailyTotalData: " + (c ? "保存成功" : "保存失败"));
                    }

                    RecordsSleepDataBaseUtil.saveAll(recordsSleeps);
                    Log.d(TAG, "RecordsSleep: " + LitePal.findAll(RecordsSleep.class).size());
                    byteDataList.clear();
                    dailyTotalDataList.clear();
                    recordsSleeps.clear();
                    syncData = new byte[0];
                    OnSyncResultsBack(code, pillowDevice);
                }
            }).start();

        }
    }

    public abstract void OnSyncResultsBack(boolean result, PillowDevice pillowDevice);

    public abstract void OnSyncNextData(int count, PillowDevice pillowDevice);
}
