package com.lzx;

import com.github.javafaker.Faker;
import org.influxdb.InfluxDB;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Grafana与InfluxDB集成
 *
 * @author lzx on 26/2/2021
 */
public class MockWeatherData {

    public static void main(String[] args) throws InterruptedException {

        InfluxDBUtil influxDBUtil = new InfluxDBUtil("influx", "influx", "http://192.168.13.162:8086", "influxdb", "influx_retention");
        InfluxDB influxdb = influxDBUtil.getInfluxdb();

        Faker faker = new Faker(new Locale("zh-CN"));

        String [] areas ={"南","北","东","西"};
        Integer [] altitudes =new Integer[]{500,800,1000,1500};
        int i =1;
        while (true)
        {
            System.out.println("插入第"+i+"条数据");
            int altitudes_index=(int)Math.floor(Math.random()*altitudes.length);
            int areas_index=(int)Math.floor(Math.random()*areas.length);
            HashMap<String,String> tags=new HashMap<>();
            tags.put("altitude",altitudes[altitudes_index]+"");
            tags.put("area",areas[areas_index]+"");

            HashMap<String,Object> fields=new HashMap<>();
            fields.put("temperature",faker.number().numberBetween(10,30));
            fields.put("humidity",faker.number().numberBetween(-10,10));

            influxDBUtil.insert("weather",tags,fields,System.currentTimeMillis(), TimeUnit.MILLISECONDS);

            Thread.sleep(faker.number().numberBetween(500,1000));
            i++;

        }
    }
}
