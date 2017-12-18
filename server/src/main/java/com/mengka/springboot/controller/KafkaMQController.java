package com.mengka.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mengka.springboot.config.MessageSender;
import com.mengka.springboot.util.TimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author huangyy
 * @date 2017/12/05.
 */
@Log4j2
@RestController
@RequestMapping("/v1/mq")
public class KafkaMQController {

    @Autowired
    private MessageSender messageSender;

    @Value("${kafka.topic}")
    private String topic;

    @RequestMapping("/send")
    public String send(Map<String, Object> model) throws Exception {
        String sensorAddr = "39000488";
        for (int i = 0; i < 10; i++) {
            String message = "Just for test[" + TimeUtil.toDate(new Date(), TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
            messageSender.sendMessage(topic, sensorAddr, message);
        }

        Thread.sleep(5000);

        String sensorAddr2 = "39000487";
        for (int i = 0; i < 10; i++) {
            String message = "Just for test[" + TimeUtil.toDate(new Date(), TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
            messageSender.sendMessage(topic, sensorAddr2, message);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }
}
