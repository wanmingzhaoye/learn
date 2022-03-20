package com.djt.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

public class HBaseTool {

    public static void main(String[] args) {
        //create();
        put();
    }

    public static void create() {

		//配置写到配置文件，配置中心
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.quorum", "192.168.20.121");

        Connection connection = null;
        Admin admin = null;

        try {
			//类似与mysql数据库长连接，jdbc连接mysql
            connection = ConnectionFactory.createConnection(config);

            admin = connection.getAdmin();
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("fengkong_table"));
            HColumnDescriptor columnDescriptor = new HColumnDescriptor("cf");
            tableDescriptor.addFamily(columnDescriptor);

            Integer regionNum = 500;
              // 0x7FFF 16位 32767   short 最大值
            short startkey = (short) (0x7FFF / regionNum);
            short endkey = (short)(0x7FFF - (0x7FFF / regionNum));
			
			//创建hbase表，并预计分区
            admin.createTable(tableDescriptor, Bytes.toBytes(startkey), Bytes.toBytes(endkey), regionNum);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (admin != null) {
                    admin.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void put() {
        //String mobileNo = "18911243728";


        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "192.168.20.121");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");

        Connection connection = null;
        Admin admin = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            //获取表
            Table table = connection.getTable(TableName.valueOf("fengkong_table"));
            List<Put> list = new ArrayList<Put>();
            for(int i=0;i<=100000;i++){
                String mobileNo = getTel();
                byte[] rowKeyPrefix = Bytes.toBytes((short) (mobileNo.hashCode() & 0x7FFF));
                byte[] rowKey = Bytes.add(rowKeyPrefix, Bytes.toBytes(mobileNo));
                Put put = new Put(rowKey);
                //构建PUT，属性为行键
                put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("q"), Bytes.toBytes("Y"));
                list.add(put);
                if(list.size()>=1000){
                    table.put(list);
                    list.clear();
                    System.out.println("输出1000条数据----------------------");
                }
            }

            table.put(list);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (admin != null) {
                    admin.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 返回手机号码
     */
    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
}