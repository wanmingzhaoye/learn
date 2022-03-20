package com.lzx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * influxdb工具类
 *
 * @author lzx on 25/2/2021
 */
public class InfluxDBUtil {
    //用户名
    private String username;

    //密码
    private String password ;

    //连接地址
    private  String openurl;

    //数据库
    private String database;

    //保留策略
    private String retentionPolicy;

    private InfluxDB influxdb;

    public InfluxDBUtil(String username,String password,String openurl,String database,String retentionPolicy)
    {
        this.username=username;
        this.password=password;
        this.database=database;
        this.openurl=openurl;
        this.retentionPolicy=retentionPolicy;
    }

    /**
    *
    * 获取连接influx数据库句柄
    * */

    public InfluxDB getInfluxdb()
    {
        if (influxdb==null)
        {
             influxdb = InfluxDBFactory.connect(openurl, username, password);

        }
        return  influxdb;
    }

    /**
     * 测试连接influxdb是否正常
     * */
    public boolean ping()
    {
        boolean isconnet =false;
        Pong ping =influxdb.ping();
        if (ping!=null)
        {
            isconnet=true;
        }
        return isconnet;
    }

    /**
     *
     * 插入数据
     * */
    public void insert(String measurement, Map<String,String> tags, Map<String,Object> fields, Long time , TimeUnit timeunit)
    {
        //Point 每条记录
        Point.Builder bulider = Point.measurement(measurement);
        bulider.tag(tags);
        bulider.fields(fields);
        if (0!=time)
        {
            bulider.time(time,timeunit);
        }

        influxdb.write(database,retentionPolicy,bulider.build());
    }

    /**
     * 批量插入
     * */

    public void bacthinsertPoint(BatchPoints batchPoints)
    {
       influxdb.write(batchPoints);
    }

    /**
     * 查询
     * */
    public QueryResult query(String sql){
        return influxdb.query(new Query(sql,database));
    }
    /**
     * 删除
     * */
    public String delet(String sql){
        QueryResult query = influxdb.query(new Query(sql, database));
        return query.getError();
    }

}
