package com.mengka.springboot.config;

import com.alibaba.fastjson.JSON;
import com.mengka.springboot.partition.SensorPartitioner;
import com.mengka.springboot.partition.SimplePartitioner;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyy
 * @description
 * @data 2016/12/02.
 */
@Slf4j
@EnableKafka
@Configuration
public class SenderConfig {

    @Value("${kafka.broker}")
    private String bootstrapServers;

    @Bean
    public Map producerConfigs() {
        Map props = new HashMap();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        //producer分区机制
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SensorPartitioner.class.getCanonicalName());
        props.put("partitions.1", "USA");
        props.put("partitions.2", "India");

        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);//限制客户端在单个连接上能够发送的未响应请求的个数。设置此值是1表示kafka broker在响应请求之前client不能再向同一个broker发送请求,避免消息乱序
        log.info("senderConfig = " + JSON.toJSONString(props));
        return props;
    }

    @Bean
    public ProducerFactory producerFactory() {
        return new DefaultKafkaProducerFactory(producerConfigs());
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }

    @Bean
    public MessageSender riskSender() {
        return new MessageSender();
    }

}
