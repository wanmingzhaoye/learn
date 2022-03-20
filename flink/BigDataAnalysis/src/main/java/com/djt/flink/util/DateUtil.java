package com.djt.flink.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bigdata
 * @create 2021-08-26-8:36
 */
public class DateUtil {
    public static String str2str(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String dt = sdf2.format(date);
        return dt;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.str2str("2021-08-24 21:55:40"));
    }
}
