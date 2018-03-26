package com.mengka.springboot.component;

import com.mengka.springboot.controller.TestDataController;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-06
 * @since cabbage-forward2.0
 */
@Log4j2
public class SensorAddressInitialize {

    static final Logger logger = LogManager.getLogger(TestDataController.class);

    private static final AtomicInteger sensorNum_heartbeat = new AtomicInteger(0);
    private static final AtomicInteger sensorNum_arrive = new AtomicInteger(0);
    private static final AtomicInteger sensorNum_departure = new AtomicInteger(0);

    public String getNewAddress_heartbeat(){
        int t1 = sensorNum_heartbeat.incrementAndGet();
        t1 = t1%100000;

        int charInt2 = Integer.parseInt("51A00001", 16);
        String address = Integer.toHexString(charInt2 + t1).toUpperCase();
        logger.info("------, getNewAddress_heartbeat initBerthData: " + address);

        return address;
    }

    public String getNewAddress_arrive(){
        int t1 = sensorNum_arrive.incrementAndGet();
        t1 = t1%100000;

        int charInt2 = Integer.parseInt("51A00001", 16);
        String address = Integer.toHexString(charInt2 + t1).toUpperCase();
        logger.info("------, sensorNum_arrive initBerthData: " + address);

        return address;
    }

    public String getNewAddress_departure(){
        int t1 = sensorNum_departure.incrementAndGet();
        t1 = t1%100000;

        int charInt2 = Integer.parseInt("51A00001", 16);
        String address = Integer.toHexString(charInt2 + t1).toUpperCase();
        logger.info("------, sensorNum_departure initBerthData: " + address);

        return address;
    }

    public static SensorAddressInitialize getInitialize() {
        return SensorAddressInitialize_HolderHolder.forwardExecuterInitialize_Holder;
    }

    private static class SensorAddressInitialize_HolderHolder {
        private static final SensorAddressInitialize forwardExecuterInitialize_Holder = new SensorAddressInitialize();
    }
}
