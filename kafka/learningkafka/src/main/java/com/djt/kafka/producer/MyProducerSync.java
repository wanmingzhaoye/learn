package com.djt.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author dajiangtai
 * @create 2020-12-18-13:46
 * 同步
 */
public class MyProducerSync {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();

        //props.put("bootstrap.servers","hadoop3-1:9092");
        //记不住参数名可以用ProducerConfig
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop3-1:9092");

        //消息可靠性
        props.put("acks","all");

        //重试次数
        props.put("retries",1);

        //批次大小
        props.put("batch.size",16384);

        //等待时间
        props.put("linger.ms",2);

        //RecordAccumulator缓冲区大小
        props.put("buffer.memory",33554432);

        //key  value 序列化类
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String , String> producer= new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            //send方法返回的是一个Future对象，根据 Futrue对象的特点，只需在调用 Future对象的 get方法即可
            //实现同步。
            producer.send(new ProducerRecord<>("djt","key"+i,"value"+i)).get() ;
        }
        producer.close();
    }
}
