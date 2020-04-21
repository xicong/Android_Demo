package com.wyl.monitor.entity.pneumatic_bed;

import java.io.Serializable;
import java.util.Arrays;

public class RealTimeRedData implements Serializable {
    private long time;
    private int reStartTime;	//唤醒时间
    private int direction;  //按摩方向
    private int position; //按摩位置，bit0-bit3: 头、腰、退；0不按摩，1按摩。全部为0时全身按摩
    private int Strength; //按摩强度
    private int settime; //设置定时时间【两个字节】
    private boolean inStopping; //是否在停止状态
    private int sleepStatus; //睡眠状态（0：离床，1：在床，2：清醒，3：浅睡，4：深睡）
    private boolean isAutoMode; //（0：定时模式，1：自动模式）
    private int timeCounter;//定时模式下床还剩下工作时间单位为分钟（TimeCounter[0]与TimeCounter[1]组成一个16位数据来表示时长）
    private boolean isTotalling;//统计中( 0：没有在进行数据统计，1：正在进行数据统计中)
    private int currentHeartRate;//当前心率
    private int currentRespiratoryRate;//当前呼吸率
    private boolean snore;//是否在打鼾
    private boolean isMove;//是否体动
    private String retain;//保留位
    private int isSecMove;//秒体动标识
    private int sleepingPosture;//睡姿(0-平躺,1-侧睡)
    private int positivePole;//静电正
    private int cathode;//静电负

    private int xl[];//心率数据
    private int hx[];//呼吸数据
    private int dh[];//打鼾数据
    private int td[];//体动数据
    private int intensity[];//每个气囊强度

    public RealTimeRedData(){

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getReStartTime() {
        return reStartTime;
    }

    public void setReStartTime(int reStartTime) {
        this.reStartTime = reStartTime;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStrength() {
        return Strength;
    }

    public void setStrength(int strength) {
        Strength = strength;
    }

    public int getSettime() {
        return settime;
    }

    public void setSettime(int settime) {
        this.settime = settime;
    }

    public boolean isInStopping() {
        return inStopping;
    }

    public void setInStopping(int inStoppingData) {
        this.inStopping = inStoppingData == 1 ? true : false;
    }

    public int getSleepStatus() {
        return sleepStatus;
    }

    public void setSleepStatus(int sleepStatus) {
        this.sleepStatus = sleepStatus;
    }

    public boolean isAutoMode() {
        return isAutoMode;
    }

    public void setAutoMode(int autoModeData) {
        isAutoMode = autoModeData == 1 ? true : false;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(int timeCounter) {
        this.timeCounter = timeCounter;
    }

    public boolean isTotalling() {
        return isTotalling;
    }

    public void setTotalling(int totallingData) {
        isTotalling = totallingData == 1 ? true : false;
    }

    public void setSnore(boolean snore) {
        this.snore = snore;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    public int getCurrentHeartRate() {
        return currentHeartRate;
    }

    public void setCurrentHeartRate(int currentHeartRate) {
        this.currentHeartRate = currentHeartRate;
    }

    public int getCurrentRespiratoryRate() {
        return currentRespiratoryRate;
    }

    public void setCurrentRespiratoryRate(int currentRespiratoryRate) {
        this.currentRespiratoryRate = currentRespiratoryRate;
    }

    public boolean isSnore() {
        return snore;
    }

    public void setSnore(int snoreData) {
        this.snore = snoreData == 1 ? true : false;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(int moveData) {
        isMove = moveData == 1 ? true : false;
    }

    public String getRetain() {
        return retain;
    }

    public void setRetain(String retain) {
        this.retain = retain;
    }

    public int getIsSecMove() {
        return isSecMove;
    }

    public void setIsSecMove(int isSecMove) {
        this.isSecMove = isSecMove;
    }

    public int[] getXl() {
        return xl;
    }

    public void setXl(int[] xl) {
        this.xl = xl;
    }

    public int[] getHx() {
        return hx;
    }

    public void setHx(int[] hx) {
        this.hx = hx;
    }

    public int[] getDh() {
        return dh;
    }

    public void setDh(int[] dh) {
        this.dh = dh;
    }

    public int[] getTd() {
        return td;
    }

    public void setTd(int[] td) {
        this.td = td;
    }

    public void setIntensity(int[] intensity) {
        this.intensity = intensity;
    }

    public int[] getIntensity() {
        return intensity;
    }

    public int getPositivePole() {
        return positivePole;
    }

    public void setPositivePole(int positivePole) {
        this.positivePole = positivePole;
    }

    public int getCathode() {
        return cathode;
    }

    public void setCathode(int cathode) {
        this.cathode = cathode;
    }

    public int getSleepingPosture() {
        return sleepingPosture;
    }

    public void setSleepingPosture(int sleepingPosture) {
        this.sleepingPosture = sleepingPosture;
    }

    @Override
    public String toString() {
        return "RealTimeRedData{" +
                "time=" + time +
                ", reStartTime=" + reStartTime +
                ", direction=" + direction +
                ", position=" + position +
                ", Strength=" + Strength +
                ", settime=" + settime +
                ", inStopping=" + inStopping +
                ", sleepStatus=" + sleepStatus +
                ", isAutoMode=" + isAutoMode +
                ", timeCounter=" + timeCounter +
                ", isTotalling=" + isTotalling +
                ", currentHeartRate=" + currentHeartRate +
                ", currentRespiratoryRate=" + currentRespiratoryRate + "\n" +
                ", snore=" + snore +
                ", isMove=" + isMove +
                ", retain='" + retain + '\'' +
                ", isSecMove=" + isSecMove +
                ", positivePole=" + positivePole +
                ", cathode=" + cathode +
                ", sleepingPosture=" + sleepingPosture + "\n" +
                ", xl=" + Arrays.toString(xl) + "\n" +
                ", hx=" + Arrays.toString(hx) + "\n" +
                ", dh=" + Arrays.toString(dh) + "\n" +
                ", td=" + Arrays.toString(td) + "\n" +
                ", intensity=" + Arrays.toString(intensity) +  "\n" +
                '}';
    }
}
