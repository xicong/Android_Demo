package com.wyl.monitor.analysis.CommandCallBack;

import android.util.Log;

import com.wyl.monitor.analysis.OnRealTimeDataListener;
import com.wyl.monitor.analysis.OnUpgradeCallBack.OnUpgradeListener;
import com.wyl.monitor.analysis.ParameterCallBack.OnParameterListener;
import com.wyl.monitor.analysis.ParameterCallBack.Parameter;
import com.wyl.monitor.analysis.PillowDevice;
import com.wyl.monitor.analysis.SyncCallBack.OnSyncCommandListener;
import com.wyl.monitor.analysis.VersionsCallBack.OnInquireVersionsListener;
import com.wyl.monitor.analysis.VersionsCallBack.VersionMessage;
import com.wyl.monitor.entity.pneumatic_bed.RealTimeRedData;
import com.wyl.monitor.util.HexString;

public class OnCommandCallBack implements OnCommandListener{

    private PillowDevice pillowDevice;
    public String mac = "";

    public void setPillowDevice(PillowDevice pillowDevice) {
        this.pillowDevice = pillowDevice;
        this.mac = pillowDevice.getMac();
    }

    /**
     * 版本号回调-- FEEFA0
     * @param data
     * @param object
     * @return
     */
    @Override
    public VersionMessage OnVersionBack(byte[] data,Object object) {

        StringBuffer sbu = new StringBuffer();
        byte[] nameDataByte = new byte[10];
        System.arraycopy(data, 4, nameDataByte, 0, nameDataByte.length);
        for (int i = 0; i < nameDataByte.length; i++) {
            sbu.append((char) Integer.parseInt(HexString.toHexOneString(nameDataByte[i]), 16));
        }
        VersionMessage  versionMessage = new VersionMessage();
        versionMessage.setFirmwareVersionName(sbu.toString());
        versionMessage.setMajorVersionNumber(Integer.parseInt(HexString.toHexOneString(data[14]), 16));
        versionMessage.setVersionNumber(Integer.parseInt(HexString.toHexOneString(data[15]), 16));
        versionMessage.setRevisionNumber(Integer.parseInt(HexString.toHexOneString(data[16]), 16));

        OnInquireVersionsListener onInquireVersionsListener = (OnInquireVersionsListener) object;
        if (onInquireVersionsListener != null) {
            onInquireVersionsListener.OnBackVersions(versionMessage);
        }

        return versionMessage;
    }

    /**
     * 同步数据返回 -- FEEFA2
     * @param data
     * @param object
     * @return
     */
    @Override
    public void OnSynsDataBack(byte[] data, Object object) {
        OnSyncCommandListener onSyncCommandListener = (OnSyncCommandListener) object;
        if(onSyncCommandListener!=null)
        onSyncCommandListener.OnContinueTransmit(data);
    }

    /**
     * 同步数据完回调校验 -- FEEFA3
     * @param data
     * @param object
     * @return
     */
    @Override
    public void OnSynsDataCheckBack(byte[] data, Object object) {
        OnSyncCommandListener onSyncCommandListener = (OnSyncCommandListener) object;
        byte[] crc = new byte[]{data[4], data[5], data[6], data[7]};
        if (onSyncCommandListener != null)
            onSyncCommandListener.OnCheckout(HexString.byteArrayToInt(crc));
    }

    /**
     * 固件升级请求回调 -- FEEF90
     * @param data
     * @param object
     * @return
     */
    @Override
    public void OnUpgradeFirmwareBack(byte[] data,Object object) {
        OnUpgradeListener onUpgradeListener = (OnUpgradeListener) object;
        if(onUpgradeListener!=null)
        onUpgradeListener.BeganTransport(pillowDevice);
    }

    /**
     * 返回当前固件的包数 -- FEEF91
     * @param data
     * @param object
     * @return
     */
    @Override
    public void OnFirmwarePackageCount(byte[] data,Object object) {
        OnUpgradeListener onUpgradeListener = (OnUpgradeListener) object;
        int count = ((data[5] & 0xFF) | ((data[4] & 0xFF) << 8));
        if (onUpgradeListener != null)
            onUpgradeListener.ContinueTransmit(count, pillowDevice);
    }

    /**
     * 固件升级结果 -- FEEF92
     * @param data
     * @param object
     * @return
     */
    @Override
    public void OnUpgradeFirmwareResultsBack(byte[] data,Object object) {
        OnUpgradeListener onUpgradeListener = (OnUpgradeListener) object;
        if (onUpgradeListener != null)
            onUpgradeListener.UpgradeResults((data[4] & 0xFF) == 1 ? true : false, pillowDevice);
    }

    /**
     * 实时数据 -- FEEFB0
     * @param data
     * @param object
     * @return
     */
    @Override
    public void OnRealTimeRedDataBack(byte[] data, Object object) {
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
            xlOrTd[i] = data[10 + i] & 0xff;
            int hxVa = (data[30 + i] & 0xff);
            hx[i] = hxVa > 2 ? hxVa / 2 : hxVa;

            dh[i] = data[50 + i] & 0xff;
        }

        realTimeRedData.setXl(xlOrTd);
        realTimeRedData.setHx(hx);
        realTimeRedData.setDh(dh);
        Log.e("", "实时数据" + realTimeRedData.toString());

        OnRealTimeDataListener onRealTimeDataListener = (OnRealTimeDataListener) object;
        if(onRealTimeDataListener!=null)
            onRealTimeDataListener.OnRealTimeData(realTimeRedData);
    }

    /**
     * 查询设备参数返回 -- FEEFB1
     * @param data
     * @param object
     * @return
     */
    @Override
    public Parameter OnParameterBack(byte[] data,Object object) {
        Parameter parameter = new Parameter();
        byte[] timeByte = new byte[]{data[4], data[5], data[6], data[7]};
        long time = HexString.byteArrayToInt(timeByte);
        time = time * 1000l;
        parameter.setDeviceTime(time);
        parameter.setOpenData((data[8] & 0xff) == 1 ? true : false);
        parameter.setStatistics((data[9] & 0xff) == 1 ? true : false);
        parameter.setElectric((data[10] & 0xff) == 1 ? true : false);
        parameter.setSideHeight((data[11] & 0xff));

        OnParameterListener onParameterListener = (OnParameterListener) object;

     /*   if (!isOpenData && !isSynchronousData) {
            sendSetParam(System.currentTimeMillis(), !isOpenData, isStatistics);
        }*/
        if(onParameterListener!=null)
            onParameterListener.OnParameter(parameter);

        return parameter;
    }
}
