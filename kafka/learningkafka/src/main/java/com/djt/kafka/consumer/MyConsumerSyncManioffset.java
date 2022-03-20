package com.djt.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author dajiangtai
 * @create 2020-12-18-14:59
 * 同步手动提交
 */
public class MyConsumerSyncManioffset {
    public static void main(String[] args) {
        Properties props = new Properties();

        //props.put("bootstrap.servers","hadoop3-1:9092");
        // 记不住参数名可以用ProducerConfig
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop3-1:9092");

        //指定group_id，相同group_id的消费者就属于一个组
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"t-behavior");

        //初次消费时从哪里开始消费
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        //开启自动提交
       // props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        //关闭自动提交
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");

        //指定key和value的反序列化器
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        //创建消费者
        KafkaConsumer<String,String> consumer=new KafkaConsumer<>(props);

        //订阅主题
        consumer.subscribe(Arrays.asList("test"));

        //循环获取消息
        while (true){
            ConsumerRecords<String,String> records=consumer.poll(Duration.ofSeconds(5));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d,partition = %s, key = %s, value = %s%n",record.offset(),record.partition(),record.key(),record.value());
            }

            //同步提交
            consumer.commitSync();
        }
    }
}
