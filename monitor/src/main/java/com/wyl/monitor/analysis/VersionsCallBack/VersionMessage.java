package com.wyl.monitor.analysis.VersionsCallBack;

/**
 * Creadted by WangYL on 2020/3/20.
 * Describle
 */
public class VersionMessage {

    private  int majorVersionNumber; //主板本号
    private String firmwareVersionName;//固件版本描叙
    private int versionNumber;//版本号
    private int revisionNumber;//修订号


    public int getMajorVersionNumber() {
        return majorVersionNumber;
    }

    public void setMajorVersionNumber(int majorVersionNumber) {
        this.majorVersionNumber = majorVersionNumber;
    }

    public String getFirmwareVersionName() {
        return firmwareVersionName;
    }

    public void setFirmwareVersionName(String firmwareVersionName) {
        this.firmwareVersionName = firmwareVersionName;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @Override
    public String toString() {
        return "VersionMessage{" +
                "majorVersionNumber=" + majorVersionNumber +
                ", firmwareVersionName='" + firmwareVersionName + '\'' +
                ", versionNumber=" + versionNumber +
                ", revisionNumber=" + revisionNumber +
                '}';
    }
}
