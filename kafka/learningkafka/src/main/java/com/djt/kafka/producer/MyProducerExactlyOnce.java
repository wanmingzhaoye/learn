package com.djt.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author dajiangtai
 * @create 2020-12-20-9:40
 * At Least Once+幂等性实现Exactly Once
 * 注意：可以保证同一个会话Exactly Once，无法保证跨会话的Exactly Once
 * 只适合内部发送失败自动重试
 */
public class MyProducerExactlyOnce {
    public static void main(String[] args) {
        Properties props = new Properties();

        //开启幂等性
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop3-1:9092");

        //At Least Once
        // 当 enable.idempotence 为true，这里默认为 all
        props.put("acks", "all");

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

        producer.send(new ProducerRecord<>("test","value1")) ;

        producer.close();
    }
}
