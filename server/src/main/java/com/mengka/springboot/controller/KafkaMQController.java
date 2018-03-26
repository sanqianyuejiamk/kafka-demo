package com.mengka.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mengka.springboot.component.RedisComponent;
import com.mengka.springboot.component.SensorAddressInitialize;
import com.mengka.springboot.config.MessageSender;
import com.mengka.springboot.manager.CabbageProManager;
import com.mengka.springboot.model.CabbageProReq;
import com.mengka.springboot.util.TimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author huangyy
 * @date 2017/12/05.
 */
@Log4j2
@RestController
@RequestMapping("/v1/mq")
public class KafkaMQController {

    private static final SensorAddressInitialize sensorAddressInitialize = SensorAddressInitialize.getInitialize();

    private static final String CABBAGE_PRO_SEQ_KEY = "CABBAGE_PRO_SEQ_KEY_T1";

    @Autowired
    private MessageSender messageSender;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.forward.topic}")
    private String forwardTopic;

    @Value("${kafka.noc.topic}")
    private String nocTopic;

    @Autowired
    private CabbageProManager cabbageProManager;

    @Autowired
    private RedisComponent redisComponent;

    @RequestMapping("/send")
    public String send(Map<String, Object> model) throws Exception {
        String sensorAddr = "39000488";
//        for (int i = 0; i < 10; i++) {
//            String message = "Just for test[" + TimeUtil.toDate(new Date(), TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
//            messageSender.sendMessage(topic, sensorAddr, message);
//        }
//
//        Thread.sleep(5000);
//
//        String sensorAddr2 = "39000487";
//        for (int i = 0; i < 10; i++) {
//            String message = "Just for test[" + TimeUtil.toDate(new Date(), TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
//            messageSender.sendMessage(topic, sensorAddr2, message);
//        }

        Thread.sleep(7000);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    /**
     * 发送转发程序消息
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/send6")
    public String send6(Map<String, Object> model, Integer type) throws Exception {
        log.info("send cabbageforward message..");
        String sensorAddr2 = "310174B6";

        /**
         *  "9003", "发送设备心跳", "sendDeviceStatus"
         */
        String message1 = "9003&0702&330110&100AP17&100004E1&2&20180129161148&-500&0.00&0.00&0&1";

        /**
         *  "9001", "发送泊位状态", "sendBerthStatus"
         */
        String message2 = "9001&0702&330110&100AP17&100004&2&20180129161148&500&0&101&0&1&1";

        /**
         *  "9002", "发送泊位心跳", "sendBerthHeart"
         */
        String message3 = "9002&0702&330110&100AP17&100004&2&20180129161148&500&0&102&0";

        /**
         *  "9004", "发送标签信息", "sendTagInfo"
         */
        String message4 = "9004&0702&330110&100AP17&100004&2&2&500&0&101&2&1&2";

        String message = "";
        if (type == 1) {
            message = message1;
        } else if (type == 2) {
            message = message2;
        } else if (type == 3) {
            message = message3;
        } else if (type == 4) {
            message = message4;
        }
        messageSender.sendMessage("SPDS_HZDQ", sensorAddr2, message);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    @RequestMapping("/send7")
    public String send7(Map<String, Object> model, Integer type) throws Exception {
        log.info("send noc message..");

        //RF传感器
        String message = "3001000038000000001100056332aa00381202180e0e04040604060001000100010001000100010001e2a11f0000000000000034363030343034313231303031323600ffffffffffff";

        /**
         * 60: NB传感器
         *
         * 39000044: 传感器地址
         *
         * 心跳事件: 60 39000044 3A [11]
         *
         * 有车: 60 39000044 3A 11 AA 20 11 09 1C 15 17 38 A8 00 A4 00 [18]
         */
        //NB传感器(有车心跳数据)
        String message2 = "60390000443A11AA2011091C151738A800A40018FBC7FE92FFE300EE073210771D000000001A3C2500";

        String messgae3 = "300100000400000000000000e130aa006e12030110020d0001000000000001000012030110020d1964c810303030303030453100000000000000000000000000000000000000000000000000000000000000000000000012030110020d12030110020d00000000000000000000000000000000000000000000000000000000";

        messageSender.sendMessage(nocTopic, "11", messgae3);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    /**
     * http://127.0.0.1:8053/v1/mq/send8?type=heartbeat&car=1&seq=61
     * http://127.0.0.1:8053/v1/mq/send8?type=arrival&car=1&seq=62
     * http://127.0.0.1:8053/v1/mq/send8?type=departure&car=1&seq=69
     *
     * @param model
     * @param type
     * @param car
     * @param seq
     * @return
     * @throws Exception
     */
    @RequestMapping("/send8")
    public String send7(Map<String, Object> model, String type,Integer car,Integer seq) throws Exception {
        log.info("send noc message..");

        String address = "310174B6";

        if ("heartbeat".equals(type) && car == 1) {
            cabbageProManager.sendHeartBeatMessgae_NB(address,seq);
        }else if("arrival".equals(type)){
            cabbageProManager.sendArrivalMessgae_NB(address,seq);
        }else if("departure".equals(type)){
            cabbageProManager.sendDepartureMessage_NB(address,seq);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/sendtest8", method = RequestMethod.POST)
    public String sendtest8(Map<String, Object> model,@RequestBody CabbageProReq cabbageProReq) throws Exception {
        log.info("send noc message..");

        String address = "310174B6";

        if ("heartbeat".equals(cabbageProReq.getType()) && cabbageProReq.getCar() == 1) {
            cabbageProManager.sendHeartBeatMessgae_NB(address,cabbageProReq.getSeq());
        }else if("arrival".equals(cabbageProReq.getType())){
            cabbageProManager.sendArrivalMessgae_NB(address,cabbageProReq.getSeq());
        }else if("departure".equals(cabbageProReq.getType())){
            cabbageProManager.sendDepartureMessage_NB(address,cabbageProReq.getSeq());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    @RequestMapping("/t_heartbeat")
    public String heartbeat(Map<String, Object> model) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1000);

        for(int i=0;i<1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取传感器地址
                    String address = sensorAddressInitialize.getNewAddress_heartbeat();

                    long seq = redisComponent.incCacheMap(CABBAGE_PRO_SEQ_KEY,address,1);
                    seq = seq%255;

                    cabbageProManager.sendHeartBeatMessgae_NB(address,(int)seq);

                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    @RequestMapping("/t_arrive")
    public String arrive(Map<String, Object> model) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1000);

        for(int i=0;i<1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取传感器地址
                    String address = sensorAddressInitialize.getNewAddress_arrive();

                    long seq = redisComponent.incCacheMap(CABBAGE_PRO_SEQ_KEY,address,1);
                    seq = seq%255;

                    cabbageProManager.sendArrivalMessgae_NB(address,(int)seq);

                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }

    @RequestMapping("/t_departure")
    public String departure(Map<String, Object> model) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1000);

        for(int i=0;i<1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取传感器地址
                    String address = sensorAddressInitialize.getNewAddress_departure();

                    long seq = redisComponent.incCacheMap(CABBAGE_PRO_SEQ_KEY,address,1);
                    seq = seq%255;

                    cabbageProManager.sendDepartureMessage_NB(address,(int)seq);

                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("message", "send success");
        return jsonObject.toJSONString();
    }
}
