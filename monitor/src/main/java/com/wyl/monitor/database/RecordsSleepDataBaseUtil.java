package com.wyl.monitor.database;


import com.wyl.monitor.entity.pneumatic_bed.DailyTotalData;
import com.wyl.monitor.entity.pneumatic_bed.RecordsSleep;

import org.litepal.LitePal;

import java.util.List;

/**
 * 记录表的增删改操作
 */
public class RecordsSleepDataBaseUtil {


    /**
     * 查询全部记录
     * @param
     * @return
     */
    public static List<RecordsSleep> findAll(){
        List<RecordsSleep> recordsSleepList  =  LitePal.findAll(RecordsSleep.class);
        if(recordsSleepList.size() > 0){
            return recordsSleepList;
        }else{
            return null;
        }
    }


    /**
     * 保存一条记录
     * @param recordsSleep
     * @return
     */
    public static boolean save(RecordsSleep recordsSleep){
        return  recordsSleep.save();
    }


    public static void saveAll(List<RecordsSleep> recordsSleeps){
        LitePal.saveAll(recordsSleeps);
    }

    /**
     * 查询指定时间段的记录
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<RecordsSleep> findAppointTime (String startTime,String endTime){
        List<RecordsSleep>  recordsSleepList = LitePal.where( " time between ? and ?", startTime, endTime).find(RecordsSleep.class);
        return recordsSleepList;
    }

    /**
     * 删除指定时间段的记录
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean deleteAppointTime (String startTime,String endTime){
        int result = LitePal.deleteAll(RecordsSleep.class, " time between ? and ?", startTime, endTime);
        return  result > 0? true : false;
    }



    public static RecordsSleep findLastData(String mac){
        RecordsSleep recordsSleep = LitePal.where("mac == ? ",mac).findLast(RecordsSleep.class);
        return recordsSleep;
    }

}
