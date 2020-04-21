package com.wyl.monitor.analysis.OnUpgradeCallBack;

import com.wyl.monitor.analysis.PillowDevice;

public interface OnUpgradeListener {

    /**
     * 开始传输
     */
    void BeganTransport(PillowDevice pillowDevice);

    /**
     * 继续传输
     * @param count 当前计数
     */
    void ContinueTransmit(int count,PillowDevice pillowDevice);


    /**
     * 是否升级成功
     * @param results
     */
     void UpgradeResults(boolean results,PillowDevice pillowDevice);

    /**
     * 超时重发
     */
    void TimeoutRetransmission();


}
