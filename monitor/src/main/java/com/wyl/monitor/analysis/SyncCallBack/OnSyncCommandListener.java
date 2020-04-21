package com.wyl.monitor.analysis.SyncCallBack;

import com.wyl.monitor.analysis.PillowDevice;

public interface OnSyncCommandListener {

    void OnContinueTransmit(byte[] data);

    void OnCheckout(int crc32);

    void OnSyncOvertime();
}
