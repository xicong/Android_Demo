package com.wyl.monitor.database;



import com.wyl.monitor.entity.pneumatic_bed.DailyTotalData;

import org.litepal.LitePal;

import java.util.List;

/**
 * 记录数据统计表的增删改操作
 */
public class DailyTotalDataBaseUtil {

    public static boolean save(DailyTotalData dailyTotalData) {
        boolean result = dailyTotalData.save();
        return result;
    }

    public static void saveAll(List<DailyTotalData> dailyTotalDatas) {
        LitePal.saveAll(dailyTotalDatas);
    }


    /**
     * 查询全部记录的统计
     *
     * @param
     * @return
     */
    public static List<DailyTotalData> findAll() {
        List<DailyTotalData> dailyTotalDataList = LitePal.findAll(DailyTotalData.class);
        return dailyTotalDataList;
    }

    /**
     * 查询指定时间记录的统计
     *
     * @param startTime
     * @return
     */
    public static DailyTotalData find(String startTime, String endTime) {

  /*      int max = LitePal.where("startTime >= ? or endTime <= ? ", startTime, endTime)
                .max(DailyTotalData.class, "sleepTime", int.class);*/
        int max = LitePal.where("date(?) == date(startTime)",startTime)
                .max(DailyTotalData.class, "sleepTime", int.class);
        List<DailyTotalData> dailyTotalDataList = LitePal.where("date(?) == date(startTime) and sleepTime == ?", startTime, max + "").find(DailyTotalData.class);
        if (dailyTotalDataList.size() > 0) {
            return dailyTotalDataList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询最后一条
     * @param mac
     * @return
     */
    public static DailyTotalData findLastData(String mac){
        DailyTotalData dailyTotalData = LitePal.where("mac == ? ",mac).findLast(DailyTotalData.class);
        return dailyTotalData;
    }

   /* *//**
     * 删除指定时间段记录的统计
     *
     * @param startTime
     * @param endTime
     * @return
     *//*
    public static List<DailyTotalData> findAll(String startTime, String endTime) {
        List<DailyTotalData> dailyTotalDataList = LitePal.where("startTime > ? and endTime < ?", startTime, endTime).find(DailyTotalData.class);
        return dailyTotalDataList;
    }
*/

    /**
     * 删除指定时间段记录的统计
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean deleteDailyTotalDatas(String startTime, String endTime) {
        int result = LitePal.deleteAll(DailyTotalData.class, "startTime = ? and endTime = ?", startTime, endTime);
        return result == 1 ? true : false;
    }

}
