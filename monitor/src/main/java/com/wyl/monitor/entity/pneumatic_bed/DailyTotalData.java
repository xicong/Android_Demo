package com.wyl.monitor.entity.pneumatic_bed;

import com.wyl.monitor.database.RecordsSleepDataBaseUtil;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DailyTotalData  extends LitePalSupport implements Serializable {

    private int id;
    private String startTime;//入睡开始时间
    private String endTime;//入睡结束时间
    private int sleepTime;//入睡时长(分钟)
    private int avgHeartRate;//平均心率
    private int maxHeartRate;//最高心率
    private int minHeartRate;//最低心率
    private int avgBreathe;//平均呼吸
    private int maxBreathe;//最高呼吸
    private int minBreathe;//最低呼吸
    private int deepSleepTime;//深睡时长（分钟）
    private int lightSleepTime;//浅睡时长（分钟）
    private int soberTime;//清醒时长
    private int snoreTime;//打鼾时长
    private int moveNumber;//体动次数
    private int totalScore;//睡眠评分
    private String mac;
    private List<RecordsSleep> recordsSleeps = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(int avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(int minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public int getAvgBreathe() {
        return avgBreathe;
    }

    public void setAvgBreathe(int avgBreathe) {
        this.avgBreathe = avgBreathe;
    }

    public int getMaxBreathe() {
        return maxBreathe;
    }

    public void setMaxBreathe(int maxBreathe) {
        this.maxBreathe = maxBreathe;
    }

    public int getMinBreathe() {
        return minBreathe;
    }

    public void setMinBreathe(int minBreathe) {
        this.minBreathe = minBreathe;
    }

    public int getDeepSleepTime() {
        return deepSleepTime;
    }

    public void setDeepSleepTime(int deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }

    public int getLightSleepTime() {
        return lightSleepTime;
    }

    public void setLightSleepTime(int lightSleepTime) {
        this.lightSleepTime = lightSleepTime;
    }

    public int getSoberTime() {
        return soberTime;
    }

    public void setSoberTime(int soberTime) {
        this.soberTime = soberTime;
    }

    public int getSnoreTime() {
        return snoreTime;
    }

    public void setSnoreTime(int snoreTime) {
        this.snoreTime = snoreTime;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<RecordsSleep> getRecordsSleeps() {
        recordsSleeps.clear();
        recordsSleeps.addAll(RecordsSleepDataBaseUtil.findAppointTime(startTime,endTime));
        return recordsSleeps;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setRecordsSleeps(List<RecordsSleep> recordsSleeps) {
        this.recordsSleeps.clear();
        this.recordsSleeps.addAll(recordsSleeps);
    }

    @Override
    public String toString() {
        return "DailyTotalData{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sleepTime=" + sleepTime +
                ", avgHeartRate=" + avgHeartRate +
                ", maxHeartRate=" + maxHeartRate +
                ", minHeartRate=" + minHeartRate +
                ", avgBreathe=" + avgBreathe +
                ", maxBreathe=" + maxBreathe +
                ", minBreathe=" + minBreathe +
                ", deepSleepTime=" + deepSleepTime +
                ", lightSleepTime=" + lightSleepTime +
                ", soberTime=" + soberTime +
                ", snoreTime=" + snoreTime +
                ", moveNumber=" + moveNumber +
                '}';
    }
}
