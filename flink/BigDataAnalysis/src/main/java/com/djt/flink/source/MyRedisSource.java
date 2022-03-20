package com.djt.flink.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bigdata
 * @create 2021-08-20-20:01
 * hset areas AREA_US US
 * hset areas AREA_CT TW,HK
 * hset areas AREA_PK KW,SA
 * hset areas AREA_IN IN
 */
public class MyRedisSource implements SourceFunction<HashMap<String,String>> {
    private final long SLEEP_MILLION=60000;
    private boolean isRunning = true;
    private Jedis jedis = null;

    @Override
    public void run(SourceContext<HashMap<String, String>> ctx) throws Exception {
        this.jedis = new Jedis("hadoop1",6379);
        HashMap<String, String> kvMap = new HashMap<>();
        while (isRunning){
            kvMap.clear();
            Map<String, String> cityCode = jedis.hgetAll("cityCode");
            for(Map.Entry<String,String> entry:cityCode.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                kvMap.put(key,value);
            }

            if(kvMap.size()>0){
                ctx.collect(kvMap);
            }
            Thread.sleep(SLEEP_MILLION);
        }
    }

    @Override
    public void cancel() {
        isRunning=false;
        if(jedis!=null){
            jedis.close();
        }
    }
}
