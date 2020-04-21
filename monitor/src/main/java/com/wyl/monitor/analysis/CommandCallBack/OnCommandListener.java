package com.wyl.monitor.analysis.CommandCallBack;

public interface OnCommandListener {

    //版本号回调 -- FEEFA0
    Object OnVersionBack(byte[] data, Object object);

    //同步数据返回 -- FEEFA2
    void OnSynsDataBack(byte[] data, Object object);

    //同步数据完回调校验 -- FEEFA3
    void OnSynsDataCheckBack(byte[] data, Object object);

    //固件升级请求回调 -- FEEF90
    void OnUpgradeFirmwareBack(byte[] data,Object object);

    //返回当前固件的包数 -- FEEF91
    void OnFirmwarePackageCount(byte[] data, Object object);

    //固件升级结果 -- FEEF92
    void OnUpgradeFirmwareResultsBack(byte[] data,  Object object);

    //实时数据 -- FEEFB0
    void OnRealTimeRedDataBack(byte[] data, Object object);

    //查询设备参数返回 -- FEEFB1
    Object OnParameterBack(byte[] data, Object object);
}
