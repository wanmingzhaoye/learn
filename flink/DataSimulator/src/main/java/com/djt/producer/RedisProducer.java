package com.djt.producer;

import com.djt.entity.DistinctCode;
import com.djt.util.DBUtil;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.List;

/**
 * 小表数据缓存到redis
 * hkeys cityCode  查看所有数据
 * hget cityCode  "HL-HH" 查看其中一个Field数据
 * del cityCode  删除key整个数据
 * @author bigdata
 * @create 2021-08-25-16:49
 */
public class RedisProducer {
    public static void main(String[] args) throws SQLException {
        String sql = "select * from distinctcode";
        List<DistinctCode> dcList = DBUtil.getDCList(sql);

        Jedis client = new Jedis("192.168.0.111", 6379);
        for (int i=0;i<dcList.size();i++){
            DistinctCode dc = dcList.get(i);
            client.hset("cityCode",dc.getCityCode(),dc.getProvinceCode());
        }

        client.close();

    }
}
