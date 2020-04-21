package com.wyl.monitor.entity.pneumatic_bed;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class RecordsSleep extends LitePalSupport implements Serializable {

    private int id;
    private int dailytotaldata_id;
    private String mac;//设备地址
    private String time;//时间
    private int electricQuantity;//电池电量（暂时没用）
    private int sleepState;//睡眠状态(0:离床,2:清醒,3:浅睡,4:深睡)
    private boolean isSnoring;//是否打鼾
    private boolean isMove;//是否体动
    private int  heartRate; // 心率
    private int respiratoryRate;//呼吸率


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDailytotaldata_id() {
        return dailytotaldata_id;
    }

    public void setDailytotaldata_id(int dailytotaldata_id) {
        this.dailytotaldata_id = dailytotaldata_id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getElectricQuantity() {
        return electricQuantity;
    }

    public void setElectricQuantity(int electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public int getSleepState() {
        return sleepState;
    }

    public void setSleepState(int sleepState) {
        this.sleepState = sleepState;
    }

    public boolean isSnoring() {
        return isSnoring;
    }

    public void setSnoring(int snoring) {
        isSnoring = snoring == 1 ?true : false;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(int move) {
        isMove = move == 1 ? true : false;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    @Override
    public String toString() {
        return "RecordsSleep{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", time='" + time + '\'' +
                ", electricQuantity=" + electricQuantity +
                ", sleepState=" + sleepState +
                ", isSnoring=" + isSnoring +
                ", isMove=" + isMove +
                ", heartRate=" + heartRate +
                ", respiratoryRate=" + respiratoryRate +
                '}';
    }
}
