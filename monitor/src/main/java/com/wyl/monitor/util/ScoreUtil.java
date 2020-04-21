package com.wyl.monitor.util;

/**
 * Created by peng on 2018/5/5.
 */

public class ScoreUtil {


    public static int countScore(int zsc,int rs, float xl, int td){
        int s = 0;
        int zsc_score,rs_score,xl_score,td_score;
        if(zsc >= 7){
            //s = s + 30;
            zsc_score = 30;
        }else if(zsc >= 6){
            //s = s + 25;
            zsc_score = 25;
        }else if(zsc >= 5){
            //s = s + 20;
            zsc_score = 20;
        }else if(zsc >= 1){
            //s = s + 15;
            zsc_score = 15;
        }else{
            //s = s + 0;
            zsc_score = 0;
        }


        if(rs < 15){
            //s = s + 20;
            rs_score = 20;
        }else if(rs < 30){
            //s = s + 18;
            rs_score = 18;
        }else if(rs < 40){
            //s = s + 16;
            rs_score = 16;
        }else if(rs < 60){
            //s = s + 10;
            rs_score = 10;
        }else{
            //s = s + 0;
            rs_score = 0;
        }

        if(xl > 84){
            //s = s + 30;
            xl_score = 30;
        }else if(xl > 74){
            //s = s + 24;
            xl_score = 24;
        }else if(xl > 64){
            //s = s + 20;
            xl_score = 20;
        }else if(xl > 30){
            //s = s + 15;
            xl_score = 15;
        }else{
            //s = s + 10;
            xl_score = 10;
        }

        if(td < 10){
            //s = s + 10;
            td_score = 10;
        }else if(td < 31){
            //s = s + 8;
            td_score = 8;
        }else if(td < 41){
            //s = s + 6;
            td_score = 6;
        }else if(td < 51){
            //s = s + 4;
            td_score = 4;
        }else{
            //s = s + 0;
            td_score = 0;
        }

        s = zsc_score + rs_score + xl_score + td_score;
        //Log.d("getScore", "s:" + s +", zsc_score:" + zsc_score +", rs_score:" + rs_score +", xl_score:" + xl_score +", td_score:" + td_score );
        return s;
    }



}
