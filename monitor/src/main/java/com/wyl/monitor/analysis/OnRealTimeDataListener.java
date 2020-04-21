package com.wyl.monitor.analysis;

import com.wyl.monitor.entity.pneumatic_bed.RealTimeRedData;

/**
 * 设备实时数据监听回调
 */
public interface OnRealTimeDataListener <T extends RealTimeRedData>{
    void OnRealTimeData(T t);
}
