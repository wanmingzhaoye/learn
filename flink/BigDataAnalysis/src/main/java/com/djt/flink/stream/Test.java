package com.djt.flink.stream;

import com.alibaba.fastjson.JSON;
import com.djt.flink.entity.Live;
import com.djt.flink.sink.MySQLSink;
import com.djt.flink.util.DateUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * @author bigdata
 * @create 2021-08-26-9:30
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //获取本地环境
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


        String inputTopic =  "liveDataClean";
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers","hadoop1:9092");
        prop.setProperty("group.id","liveDataClean");

        //{"cityCode":"XZ-AL","provinceCode":"CN-XZ","v-score":0.1,"v-level":"LV1",
        // "time":"2021-08-25 00:38:00","v-type":"chat"}
        //读取kafka数据
        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<String>(inputTopic, new SimpleStringSchema(), prop);
        DataStreamSource<String> kafkaData = env.addSource(myConsumer);

        //数据过滤清洗
        DataStream<Live> filter = kafkaData.map(new MapFunction<String, Live>() {
            @Override
            public Live map(String value) throws Exception {
                try {
                    return JSON.parseObject(value, Live.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }).filter(x -> x != null)
                .filter(new FilterFunction<Live>() {
                    @Override
                    public boolean filter(Live value) throws Exception {
                        return !"null".equals(value.getTime());
                    }
                });

        //数据统计分析
        DataStream<Tuple4<String,String, String, Integer>> sum = filter.map(new MapFunction<Live, Tuple4<String,String, String, Integer>>() {
            @Override
            public Tuple4<String,String, String, Integer> map(Live value) throws Exception {
                return Tuple4.of(DateUtil.str2str(value.getTime()),value.getProvinceCode(), value.getCityCode(), 1);
            }
        }).keyBy(new KeySelector<Tuple4<String,String, String, Integer>, Tuple3<String,String, String>>() {
            @Override
            public Tuple3<String,String, String> getKey(Tuple4<String,String, String, Integer> value) throws Exception {
                return Tuple3.of(value.f0, value.f1,value.f2);
            }
        }).sum(3);


        sum.print();

        //数据入库mysql
        sum.addSink(new MySQLSink());

        env.execute("LiveDataAnalytics");
    }
}
