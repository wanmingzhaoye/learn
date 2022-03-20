package com.djt.producer;

import com.djt.entity.DistinctCode;
import com.djt.util.DBUtil;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author bigdata
 * @create 2021-08-24-20:29
 */
public class LiveVideoProducer {
    public static void main(String[] args) throws SQLException, InterruptedException {
        //查询sql
        String sql = "select * from distinctcode";
        //获取城市编码集合
        List<String> dcList = DBUtil.getCityCodeList(sql);
        //数据落盘文件
        //String filepath="D:\\data\\livevideo.log";
        String filepath=args[0];
        while (true){
            //{"time":"2021-08-24 21:55:40","cityCode":"GD-YJ","data":[{"v-type":"chat","v-score":0.4,"v-level":"LV1"}]}
            String message ="{\"time\":\""+getCurrentTime()+"\",\"cityCode\":\""+getCityCode(dcList)+
                    "\",\"data\":[{\"v-type\":\""+getRandomType()+"\",\"v-score\":"+getRandowScore()+
                    ",\"v-level\":\""+getRandowLevel()+"\"}]}";
            System.out.println(message);
            writeFile(filepath,message);
            Thread.sleep(2000);
        }
    }

    /**
     * 生成当前时间
     * @return
     */
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取城市编码
     * @param list
     * @return
     */
    public static String getCityCode(List<String> list){
        Random random = new Random();
        int i = random.nextInt(list.size());
        return list.get(i);
    }

    /**
     * 直播类别：音乐、舞蹈、聊天互动、户外、文化才艺、美食、知识教学、其他
     * @return
     */
    public static String getRandomType(){
        String[] types = {"music","dance","chat","outdoors","culturaltalents","finefood"," knowledgeteaching","other"};
        Random random = new Random();
        int i = random.nextInt(types.length);
        return types[i];
    }

    /**
     * 直播视频打分
     * @return
     */
    public static double getRandowScore(){
        double[] types = {0.3,0.6,0.9,0.2,0.4,0.1,0.5,0.8,1.0,0.7};
        Random random = new Random();
        int i = random.nextInt(types.length);
        return types[i];
    }

    /**
     * 直播视频等级
     * @return
     */
    public static String getRandowLevel(){
        String[] types = {"LV1","LV2","LV3","LV4","LV5","LV6","LV7","LV8","LV9"};
        Random random = new Random();
        int i = random.nextInt(types.length);
        return types[i];
    }

    /**
     * 数据落盘
     * @param file
     * @param conent
     */
    public static void writeFile(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
            out.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
