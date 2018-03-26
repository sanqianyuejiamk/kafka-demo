package com.mengka.springboot.util;

import java.util.Date;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-06
 * @since cabbage-forward2.0
 */
public class CabbageProTimeUtil {

    public static String getTimeHexStr(){
        Date nowTime = new Date();
        nowTime = TimeUtil.hourBefore(nowTime);

        String time = TimeUtil.toDate(nowTime,TimeUtil.FORMAT_YYYYMMDDHHMMSS);
        int y1 = Integer.parseInt(time.substring(2,4));
        String year = Integer.toHexString(y1).toUpperCase();

        int m1 = Integer.parseInt(time.substring(4,6));
        String month = (m1<16?"0":"") + Integer.toHexString(m1).toUpperCase();

        int d1 = Integer.parseInt(time.substring(6,8));
        String day = (d1<16?"0":"") + Integer.toHexString(d1).toUpperCase();

        int h1 = Integer.parseInt(time.substring(8,10));
        String hour = (h1<16?"0":"") + Integer.toHexString(h1).toUpperCase();

        int min1 = Integer.parseInt(time.substring(10,12));
        String minute = (min1<16?"0":"") + Integer.toHexString(min1).toUpperCase();

        int sec1 = Integer.parseInt(time.substring(12,14));
        String second = (sec1<16?"0":"") + Integer.toHexString(sec1).toUpperCase();

        return year+month+day+hour+minute+second;
    }

    public static void main(String[] args){
        String tmp = getTimeHexStr();
    }
}
