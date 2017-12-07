package com.mengka.springboot.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author huangyy
 * @date 2017/12/05.
 */
@Slf4j
public class MessageSender {

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    /**
     *
     *
     * @param topic
     * @param message
     * @return
     */
    public boolean sendMessage(final String topic, final String message) {
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate
                .send(topic, message);
        future.addCallback(
                new ListenableFutureCallback<SendResult<Integer, String>>() {

                    public void onSuccess(
                            SendResult<Integer, String> result) {

                        log.info("sent message topic='{}' with offset={}",
                                topic,
                                result.getRecordMetadata().offset());
                    }

                    public void onFailure(Throwable ex) {
                        log.error("unable to send message topic='{}'",
                                topic, ex);
                    }
                });
        return true;
    }

    /**
     *
     *
     * @param topic
     * @param message
     * @return
     */
    public boolean syncSendMessage(final String topic, final String message) {
        boolean success = false;
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate
                .send(topic, message);
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<SendResult<Integer, String>> theResult = new AtomicReference<>();
        future.addCallback(
                new ListenableFutureCallback<SendResult<Integer, String>>() {

                    public void onSuccess(
                            SendResult<Integer, String> result) {
                        theResult.set(result);
                        latch.countDown();
                        log.info("sync sent message topic='{}' with offset={}",
                                topic,
                                result.getRecordMetadata().offset());
                    }

                    public void onFailure(Throwable ex) {
                        log.error("unable to sync send message topic='{}'",
                                topic, ex);
                    }
                });
        try {
            latch.await();
            SendResult<Integer, String> sendResult = theResult.get();
            if (sendResult != null) {
                if (sendResult.getRecordMetadata() != null && sendResult.getRecordMetadata().offset() > 0) {
                    success = true;
                }
            }
        } catch (InterruptedException e) {
            log.error("sync sent message error! topic=" + topic, e);
        }
        return success;
    }
}
