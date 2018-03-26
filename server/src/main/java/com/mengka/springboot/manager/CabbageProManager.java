package com.mengka.springboot.manager;

import com.mengka.springboot.config.MessageSender;
import com.mengka.springboot.util.CabbageProTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-05
 * @since cabbage-forward2.0
 */
@Log4j2
@Service
public class CabbageProManager {

    @Value("${kafka.noc.topic}")
    private String nocTopic;

    @Autowired
    private MessageSender messageSender;

    /**
     * NB传感器(有车心跳数据)
     *
     * 60390000443A11AA2011091C151738A800A40018FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     * 60 39000044 3A [11] AA 20 11 09 1C 15 17 38 A8 00 A4 00 [18] FB C7FE92FFE300EE073210771D000000001A3C2500
     *
     * [11] => 16 + 1 = 17 => ctrlCode = 17 => "心跳事件"
     * [18] => 16 + 8 = 24 => parkEvent = 24 => "车位是否占用" true , "磁检测状态是否正常" true
     */
    public void sendHeartBeatMessgae_NB(String address,int seq){
        log.info("----------, 发送NB传感器(有车心跳数据)。。"+address);

        String seqHexStr = (seq<16?"0":"")+Integer.toHexString(seq);

        String heartHeatEvent = "11";

        String template = "60%s%s"+heartHeatEvent+"AA2011091C151738A800A40018FBC7FE92FFE300EE073210771D000000001A3C2500";
        String message = String.format(template,address,seqHexStr);

        /**
         *  HeartBeatProcesser >> 当前HEARTBEAT有车 >> 但是泊位缓存数据中无车,则需要插入当前泊位停车记录；（有车泊位+1） >> 发送驶入转发消息
         *                                         >> 有车 >> 发送心跳转发消息
         */
        messageSender.sendMessage(nocTopic, "11", message);
    }

    /**
     *  60390000444410AA2011091C151738A800A40011FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     *  60 39000044 44 [10] AA 20 11 09 1C 15 17 38 A8 00 A4 00 [11] FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     *  [10] => 16 => ctrlCode = 16 => "驶入事件"
     *  [11] => 17 => parkEvent = 17 => (parkEvent & MASK_PE_EVENTTYPE) == PE_ARRIVAL
     *
     * ---------------
     *  60 39000044 3f 10 AA 20 [11] [09] [1C] [15] [17] [38] A800A40011FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     *  17年09月28日 21:23:56
     *  +8
     *  =
     *  17年09月29日 05:23:56
     *
     * @param address
     * @param seq
     */
    public void sendArrivalMessgae_NB(String address,int seq){
        log.info("----------, 发送NB传感器(驶入数据)。。");

        String seqHexStr = (seq<16?"0":"")+Integer.toHexString(seq);

        String arriveEvent = Integer.toHexString(16);

        String t1 = "11";

        String sendTime = CabbageProTimeUtil.getTimeHexStr();//11091C151738

        String template = "60%s%s"+arriveEvent+"AA20"+sendTime+"A800A400"+t1+"FBC7FE92FFE300EE073210771D000000001A3C2500";
        String message = String.format(template,address,seqHexStr);

        /**
         *  ArrivalProcesser >> 发送驶入转发消息
         *
         */
        messageSender.sendMessage(nocTopic, "11", message);
    }

    /**
     *  60390000444510AA2011091C151738A800A40012FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     *  60 39000044 [45] [10] AA2011091C151738A800A4 00 [12] FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     *  [45] => seq
     *  [12] => 18 => parkEvent = 18 => (parkEvent & MASK_PE_EVENTTYPE) == PE_DEPARTURE
     *
     * ---------------
     *  60 39000044 3f 10 AA 20 [11] [09] [1C] [15] [17] [38] A800A40011FBC7FE92FFE300EE073210771D000000001A3C2500
     *
     *  17年09月28日 21:23:56
     *  +8
     *  =
     *  17年09月29日 05:23:56
     *
     * @param address
     * @param seq
     */
    public void sendDepartureMessage_NB(String address,int seq){
        log.info("----------, 发送NB传感器(驶离数据)。。");

        String seqHexStr = (seq<16?"0":"")+Integer.toHexString(seq);

        String arriveEvent = Integer.toHexString(16);

        String t1 = "12";

        String sendTime = CabbageProTimeUtil.getTimeHexStr();//11091C151738

        String template = "60%s%s"+arriveEvent+"AA20"+sendTime+"A800A400"+t1+"FBC7FE92FFE300EE073210771D000000001A3C2500";
        String message = String.format(template,address,seqHexStr);

        /**
         *  DepartureProcesser >> 更新驶离时间 >> 停车信息插入历史停车记录表
         *                     >> 发送驶离转发信息
         */
        messageSender.sendMessage(nocTopic, "11", message);
    }
}
