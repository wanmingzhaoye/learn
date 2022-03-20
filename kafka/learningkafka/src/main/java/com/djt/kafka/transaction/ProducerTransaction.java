package com.djt.kafka.transaction;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author dajiangtai
 * @create 2020-12-20-10:29
 */
public class ProducerTransaction {
    public static void main(String[] args) {
        Properties properties= new Properties();

        //必须打开幂等特性
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        //key value 序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()) ;
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //kafka 集群地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop3-1:9092");

        // 每台机器唯一
        //指定transactionID这样可以确保相同的TransactionId返回相同的PID，
        // 用于恢复或者终止之前未完成的事务。
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"producer-1");

        //构造producer
        KafkaProducer<String , String> producer= new KafkaProducer<>(properties );

        //初始化事务
        producer.initTransactions();

        //开启事务
        producer.beginTransaction();

        String topic="test";

        try{
            //处理业务逻辑并创建ProducerRecord
            ProducerRecord<String, String> recordl =new ProducerRecord<>(topic,"msgl");
            producer.send(recordl) ;
            ProducerRecord<String, String> record2 =new ProducerRecord<>(topic,"msg2");
            producer.send(record2);
            ProducerRecord<String, String> record3 =new ProducerRecord<>(topic ,"msg3");
            producer.send(record3);
            //处理一些其他逻辑 提交事务
            producer.commitTransaction();
        }catch (ProducerFencedException e){
            // 放弃事务，类似回滚事务的操作
            producer.abortTransaction();
        }finally {
            producer.close();
        }

    }
}
