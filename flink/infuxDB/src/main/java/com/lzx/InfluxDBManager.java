package com.lzx;

import javafx.scene.effect.Light;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * influxdb操作
 *
 * @author lzx on 25/2/2021
 */
public class InfluxDBManager
{
    public static void main(String[] args) {

        InfluxDBUtil influxDBUtil = new InfluxDBUtil("influx", "influx", "http://192.168.13.162:8086", "influxdb", "influx_retention");
        InfluxDB influxdb = influxDBUtil.getInfluxdb();
        influxDBUtil.ping();
        testInfluxdb(influxDBUtil);
       // insert(influxDBUtil);
       // batchInsertPoint(influxDBUtil);
        // queryInfluxDB(influxDBUtil);

        deleteInfluxDB(influxDBUtil);


    }
    public static void testInfluxdb(InfluxDBUtil influxDBUtil)
    {
      boolean ping= influxDBUtil.ping();
      if (ping)
      {
          System.out.println("数据库连接正常");
      }else
          {
              System.out.println("数据库连接失败");
          }

    }
    public static void insert(InfluxDBUtil influxDBUtil)
    {
        HashMap<String,String> tags =new HashMap<>();
        tags.put("altitude","1000");
        tags.put("area","北");

        Map<String,Object> fields = new HashMap<>();
        fields.put("temperature",5);
        fields.put("humidity",1);

        String measurement ="weather";

        influxDBUtil.insert(measurement,tags,fields,System.currentTimeMillis(), TimeUnit.MILLISECONDS);




    }
    public static void batchInsertPoint(InfluxDBUtil influxDBUtil)
    {
        String measurement ="weather";
        String dbName = "influxdb";

        Point point1 = Point.measurement(measurement)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("altitude", "800")
                .tag("area", "北")
                .addField("temperature", 10)
                .addField("humidity", 3)
                .build();

        Point point2 = Point.measurement(measurement)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("altitude", "1000")
                .tag("area", "北")
                .addField("temperature", 5)
                .addField("humidity", 4)
                .build();

        BatchPoints batchPoints = BatchPoints.database(dbName)
                .retentionPolicy("influx_retention")
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        batchPoints.point(point1).point(point2);
        influxDBUtil.bacthinsertPoint(batchPoints);

    }
    public static void queryInfluxDB(InfluxDBUtil influxDBUtil)
    {
        QueryResult query = influxDBUtil.query("select * from weather;select * from weather where altitude='1000' and area = '北'");
        int num =1;
        for (QueryResult.Result result:query.getResults())
        {

            System.out.println("-----------打印第"+num+"个-----------");
            num++;
            List<QueryResult.Series> series = result.getSeries();
            for (QueryResult.Series serie:series)
            {
                int n =1;
                System.out.println("-----------打印第"+n+"个记录-----------");
                List<String> columns = serie.getColumns();
                List<List<Object>> values = serie.getValues();

                for (List<Object> list :values)
                {
                   for (int i =0;i<list.size();i++)
                   {
                       System.out.println(columns.get(i)+":"+list.get(i));

                   }
                }
                n++;
            }
        }

    }

    public static void deleteInfluxDB(InfluxDBUtil influxDBUtil){
        String sql = "delete from weather where altitude='5'";
        influxDBUtil.delet(sql);
    }
}
