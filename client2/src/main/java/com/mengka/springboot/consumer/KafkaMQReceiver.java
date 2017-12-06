package com.mengka.springboot.consumer;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @author huangyy
 * @date 2017/12/05.
 */
@Log4j2
public class KafkaMQReceiver {

    @KafkaListener(id = "${kafka.consumegroupid}", topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(@Payload String message) {
        if (StringUtils.isBlank(message)) {
            log.error("RiskSerBillReceiver receiveMessage is null!");
            return;
        }

        log.info("RiskSerBillReceiver received message='{}'", message);
    }
}
