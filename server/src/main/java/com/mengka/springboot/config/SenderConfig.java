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

    /**
     * 建议值：
     *  https://cwiki.apache.org/confluence/display/KAFKA/KIP-185%3A+Make+exactly+once+in+order+delivery+per+partition+the+default+producer+setting
     *
     * 测试报告：
     * https://cwiki.apache.org/confluence/display/KAFKA/An+analysis+of+the+impact+of+max.in.flight.requests.per.connection+and+acks+on+Producer+performance
     *
     * @return
     */
    @Bean
    public Map producerConfigs() {
        Map props = new HashMap();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        /**
         *  根据key的自定义分区机制
         */
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SensorPartitioner.class.getCanonicalName());
        props.put("partitions.1", "USA");
        props.put("partitions.2", "India");

        /**
         *  设置为1，保证在后一条消息发送前，前一条的消息状态已经是可知的；
         *
         * https://community.hortonworks.com/articles/80813/kafka-best-practices-1.html
         *
         *  Max.in.flight.requests.per.connection > 1:
         *  1)Gives better throughput;
         *  2)May cause out of order delivery when retry occurs;
         *  3)Excessive pipelining , drops throughput;
         *
         *  【建议值：2】
         */
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);


        /**
         *  Why change acks from 1 to all?
         *
         * (消息发送成功，但是kafka集群并未复制成功，存在不一致情况)
         *  With acks=1,
         *  we only have an at most once delivery guarantee.
         *  In particular, with acks=1, the broker could crash after acknowledging a message but before replicating it.
         *
         * 【注】：对请求延迟有较明显的影响，约2倍性能影响。
         * There is a major 2x degradation in p95 latency between acks=1 and acks=all except for 64 byte messages.
         *
         * 参考：
         * https://cwiki.apache.org/confluence/display/KAFKA/An+analysis+of+the+impact+of+max.in.flight.requests.per.connection+and+acks+on+Producer+performance
         *
         * (是否等到followers复制数据成功，再响应请求acknowledged)
         *  With acks=all,
         *  The performance analysis above shows that there is an impact to latency when moving from acks=1 to acks=all.
         *  since the followers need to fetch the newly appended data before the request is acknowledged;
         *  Regardless, we believe strong durability guarantees out of the box are worth the cost of increased latency.
         *
         *  【建议值：all】
         */
        props.put(ProducerConfig.ACKS_CONFIG,"all");

        /**
         *  发送失败重试次数
         *
         * The retries config was defaulted to 0 to ensure that internal producer retries don't introduce duplicates.
         * we should let the producer retry as much as possible since there is no correctness penalty for doing so.
         *
         * 【建议值：Integer.MAX_VALUE】
         */
        props.put(ProducerConfig.RETRIES_CONFIG,Integer.MAX_VALUE);

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
