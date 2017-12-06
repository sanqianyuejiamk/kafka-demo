package com.mengka.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mengka.springboot.config.MessageSender;
import com.mengka.springboot.util.TimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/send")
    public String send(Map<String, Object> model) {
        String message = "Just for test[" + TimeUtil.toDate(new Date(), TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
        messageSender.sendMessage("testTopic", message);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success","true");
        jsonObject.put("message","send success");
        return jsonObject.toJSONString();
    }
}
