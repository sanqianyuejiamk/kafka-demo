package com.mengka.springboot.manager;

import com.mengka.springboot.dao.domain.BerthDO;
import com.mengka.springboot.dao.domain.DeviceSensorDO;
import com.mengka.springboot.dao.persistence.BerthDOMapper;
import com.mengka.springboot.dao.persistence.DeviceSensorDOMapper;
import com.mengka.springboot.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

/**
 * @author huangyy
 * @date 2017/10/10.
 */
@Slf4j
@Service
public class SensorManager {

    @Autowired
    private BerthDOMapper berthDOMapper;

    @Autowired
    private DeviceSensorDOMapper deviceSensorDOMapper;

    /**
     *  传感器数据
     *      51A00001>>51A003E8(1000个)
     *      51A00001>>51A00BB8(3000个)
     *      51A00001>>51A07530(30000个)
     */
    public void initBerthData() {
        int charInt2 = Integer.parseInt("51A00001", 16);

        for (int i = 0; i < 100000; i++) {
            int berthCode = 118000001+i;

            BerthDO berthDO = new BerthDO();
            berthDO.setId(UUID.randomUUID().toString().replace("-",""));
            berthDO.setParkId("01dc78debc04488baf27caf958bcaa54");
            berthDO.setBerthCode(String.valueOf(berthCode));
            berthDO.setRodeCode("210395");
            berthDO.setType(1);
            berthDO.setMoveSwitch(1);
            berthDO.setIsValid(1);
            berthDO.setCreatedBy("aliyun-test");
            berthDO.setUpdatedBy("aliyun-test");
            berthDO.setCreateTime(new Date());
            berthDO.setUpdateTime(new Date());
            berthDOMapper.insert(berthDO);

            String address = Integer.toHexString(charInt2 + i).toUpperCase();
            log.info("initBerthData: " + address);
            //插入传感器数据
            insertDeviceSensor(berthDO, address);
        }
    }

    public void initBerthData_test(){
        BerthDO berthDO = new BerthDO();
        berthDO.setId(UUID.randomUUID().toString().replace("-",""));
        berthDO.setParkId("01dc78debc04488baf27caf958bcaa54");
        berthDO.setBerthCode("118000002");
        berthDO.setRodeCode("210395");
        berthDO.setType(1);
        berthDO.setMoveSwitch(1);
        berthDO.setIsValid(1);
        berthDO.setCreatedBy("aliyun-test");
        berthDO.setUpdatedBy("aliyun-test");
        berthDO.setCreateTime(new Date());
        berthDO.setUpdateTime(new Date());
        berthDOMapper.insert(berthDO);

        String address = "310174B7";

        insertDeviceSensor(berthDO,address);
    }

    public void insertDeviceSensor(BerthDO berthDO, String address) {
        String sensorId = MD5.crypt("hyy" + new Date().getTime());

        DeviceSensorDO deviceSensorDO = new DeviceSensorDO();
        deviceSensorDO.setId(sensorId);
        deviceSensorDO.setBerthId(berthDO.getId());
        deviceSensorDO.setSensorAddr(address);
        deviceSensorDO.setSensorType(1);//RF传感器
        deviceSensorDO.setNetId("1010");
        deviceSensorDOMapper.insert(deviceSensorDO);
    }

}
