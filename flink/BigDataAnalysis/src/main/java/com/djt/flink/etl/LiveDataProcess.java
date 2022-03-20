package com.djt.flink.etl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.djt.flink.source.MyRedisSource;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author bigdata
 * @create 2021-08-25-15:26
 */
public class LiveDataProcess {
    public static void main(String[] args) throws Exception {
        //获取flink 本地环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置checkpoint
        env.enableCheckpointing(60000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000);
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        //设置statebackend
        //env.setStateBackend(new RocksDBStateBackend("hdfs://hadoop1:9000/flink/checkpoints",true));

        //消费kafka liveData中的数据
        String inputTopic = "liveData";
        Properties inprop = new Properties();
        inprop.setProperty("bootstrap.servers","hadoop1:9092");
        inprop.setProperty("group.id","liveData");
        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<String>(inputTopic, new SimpleStringSchema(), inprop);
        DataStream<String> kafkaData =  env.addSource(myConsumer);

        //城市-省份数据广播

        DataStream<HashMap<String,String>> redisData = env.addSource(new MyRedisSource()).broadcast();

        //数据做join操作
        DataStream<String> broadData = kafkaData.connect(redisData).flatMap(new CoFlatMapFunction<String, HashMap<String, String>, String>() {
            private HashMap<String, String> allMap = new HashMap<String, String>();

            @Override
            public void flatMap1(String value, Collector<String> out) throws Exception {
                //{"time":"2021-08-24 21:55:40","cityCode":"GD-YJ","data":[{"v-type":"chat","v-score":0.4,"v-level":"LV1"}]}
                JSONObject jsonObject = JSONObject.parseObject(value);
                String time = jsonObject.getString("time");
                String cityCode = jsonObject.getString("cityCode");
                String provinceCode = allMap.get(cityCode);

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    jsonObject1.put("cityCode",cityCode);
                    jsonObject1.put("provinceCode", provinceCode);
                    jsonObject1.put("time", time);
                    out.collect(jsonObject1.toJSONString());
                }

            }

            @Override
            public void flatMap2(HashMap<String, String> value, Collector<String> out) throws Exception {
                this.allMap = value;
            }
        });

        //处理后的数据写入Kafka
        String outputTopic = "liveDataClean";
        Properties outprop = new Properties();
        outprop.setProperty("bootstrap.servers","hadoop1:9092");
        outprop.setProperty("transaction.timeout.ms",60000*15+"");
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(outputTopic, new KeyedSerializationSchemaWrapper<String>(new SimpleStringSchema()), outprop, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

        broadData.print();
        broadData.addSink(myProducer);

        env.execute("LiveDataProcess");

    }
}
