package com.mengka.springboot.controller;

import com.mengka.springboot.component.SensorAddressInitialize;
import com.mengka.springboot.manager.SensorManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-06
 * @since cabbage-forward2.0
 */
@Controller
@RequestMapping("/v1/testdata")
public class TestDataController {

    static final Logger logger = LogManager.getLogger(TestDataController.class);

    private static final SensorAddressInitialize sensorAddressInitialize = SensorAddressInitialize.getInitialize();

    @Autowired
    private SensorManager sensorManager;

    /**
     *  10W测试泊位
     *
     *  51A00001>>51A00BB8(100000个)
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/rate")
    public String sensor(Map<String, Object> model, Long id){
        logger.info("CommonController rate id = {}",id);
        model.put("list",null);

        sensorManager.initBerthData();

        return "product_rate";
    }

    @RequestMapping("/rate3")
    public String sensor3(Map<String, Object> model, Long id){
        logger.info("CommonController rate id = {}",id);
        model.put("list",null);

        sensorManager.initBerthData_test();

        return "product_rate";
    }

    @RequestMapping("/rate5")
    public String sensor5(Map<String, Object> model, Long id){
//        logger.info("CommonController rate id = {}",id);
        model.put("list",null);


        sensorAddressInitialize.getNewAddress_heartbeat();

        return "product_rate";
    }
}
