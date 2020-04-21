package com.wyl.monitor.analysis.ParameterCallBack;

/**
 * Creadted by WangYL on 2020/3/20.
 * Describle
 */
public class Parameter {

    private long deviceTime;
    private boolean isOpenData;//是否开启实时数据
    private boolean isStatistics;//是否开启统计数据
    private boolean isElectric;//有没有放电
    private int sideHeight;//侧睡高度

    public long getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(long deviceTime) {
        this.deviceTime = deviceTime;
    }

    public boolean isOpenData() {
        return isOpenData;
    }

    public void setOpenData(boolean openData) {
        isOpenData = openData;
    }

    public boolean isStatistics() {
        return isStatistics;
    }

    public void setStatistics(boolean statistics) {
        isStatistics = statistics;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    public int getSideHeight() {
        return sideHeight;
    }

    public void setSideHeight(int sideHeight) {
        this.sideHeight = sideHeight;
    }
}
