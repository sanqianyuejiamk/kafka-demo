package com.mengka.springboot.partition;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangyy
 * @date 2017/12/06.
 */
@Log4j2
public class SimplePartitioner implements Partitioner {

    private static Map<String, Integer> countryToPartitionMap;

    private final AtomicInteger counter = new AtomicInteger(new Random().nextInt());

    /**
     * This method will get called once for each message
     *
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List partitions = cluster.availablePartitionsForTopic(topic);
        String valueStr = (String) value;
        String countryName = ((String) value).split(":")[0];
        if (countryToPartitionMap.containsKey(countryName)) {
            //If the country is mapped to particular partition return it
            int p = countryToPartitionMap.get(countryName);
            log.info("-------, partition p = " + p);
            return p;
        } else {
            //If no country is mapped to particular partition distribute between remaining partitions
            int noOfPartitions = cluster.topics().size();
            int p = value.hashCode() % noOfPartitions + countryToPartitionMap.size();
            log.info("-------, partition noOfPartitions = " + noOfPartitions + " , p = " + p);
            return p;
        }
    }

    /**
     * This method will get called at the end and gives your partitioner class chance to cleanup
     */
    @Override
    public void close() {

    }

    /**
     * This method will gets called at the start, you should use it to do one time startup activity
     *
     * @param configs
     */
    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println("Inside CountryPartitioner.configure " + configs);
        countryToPartitionMap = new HashMap<String, Integer>();
        for (Map.Entry<String, ?> entry : configs.entrySet()) {
            String keys = entry.getKey();
            if (keys.startsWith("partitions.")) {
                String keyName = entry.getKey();
                String value = (String) entry.getValue();
                log.info("----------, configure keyName = " + keyName + " , value = " + value);
                System.out.println(keyName.substring(11));

                int paritionId = Integer.parseInt(keyName.substring(11));
                log.info("---------, configure paritionId = " + paritionId);
                countryToPartitionMap.put(value, paritionId);
            }
        }
    }
}