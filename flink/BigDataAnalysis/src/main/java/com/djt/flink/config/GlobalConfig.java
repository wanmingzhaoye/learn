package com.djt.flink.config;

import java.io.Serializable;

/**
 * 在生产上一般通过配置中心来管理
 */
public class GlobalConfig implements Serializable {
    /**
     * 数据库driver class
     */
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    /**
     * 数据库jdbc url
     */
    public static final String DB_URL = "jdbc:mysql://192.168.0.104/live";
    /**
     * 数据库user name
     */
    public static final String USER_MAME = "root";
    /**
     * 数据库password
     */
    public static final String PASSWORD = "root";

    //
    //插入mysql SQL 语句 ON DUPLICATE KEY UPDATE 有相同id变成修改
    public static String INSERTSQL = "insert into  distinctcount (time,provinceCode,cityCode,sum) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE time=VALUES(time),provinceCode=VALUES(provinceCode),cityCode=VALUES(cityCode),sum=VALUES(sum)";
    /**
     * 批量提交size
     */
    public static final int BATCH_SIZE = 2;
}
