package com.efly.platform.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ccj on 2016/1/14.
 */
public class DateUtils {

    public static String getNowDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDate.format(new Date());
    }

    public static Date stringToDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date mDate = null;
        try {
            mDate = simpleDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;

    }

    public static String toStringDateCode(String date) {

        if (TextUtils.isEmpty(date)) {
            return null;
        }
        date= date.replaceAll("-","");
        date=date.replaceAll(" ","");
        date=date.replaceAll(":","");
        return date;

    }


    public static boolean compareDate(String s1, String s2) {

        Date d1 = stringToDate(s1);
        Date d2 = stringToDate(s2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        return result >= 0;
    }

    public  static  String formateDataToSimple(String date){
        if (date==null||date.isEmpty()){
            return "";
        }
       date= date.replace("00:00:00","");
        return date;
    }


}
