package com.wyl.monitor.analysis.ParameterCallBack;

import java.text.SimpleDateFormat;

public abstract class OnParameterBack implements OnParameterListener{

    /**
     * 返回设备参数
     * @param parameter
     */
    @Override
    public void OnParameter(Parameter parameter) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String data = simpleDateFormat.format(parameter.getDeviceTime()).split(" ")[0];
        String time = simpleDateFormat.format(parameter.getDeviceTime()).split(" ")[1];
        int realTime = parameter.isOpenData() ? 1 : 0;
        int statistics = parameter.isStatistics() ? 1 : 0;
        getParameter(data,time,realTime,statistics);
    }

     public abstract void getParameter(String date,String time, int  realTime, int statistics);

}
