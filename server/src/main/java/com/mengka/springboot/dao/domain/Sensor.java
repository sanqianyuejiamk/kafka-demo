/** 
 * Project Name:cabbage 
 * File Name:Sensor.java 
 * Package Name:com.innotek.system.entity 
 * Date:2016年11月16日上午11:27:32 
 * Copyright (c) 2004-2016 All Rights Reserved. 
 * 
 */
package com.mengka.springboot.dao.domain;

import java.io.Serializable;


/** 
 * 传感器实体类
 * device_sensor表实体类 
 * 
 * @author zhuhy 
 * @version cabbage1.0,2016-11-16
 * @since cabbage1.0  
 */
public class Sensor implements Serializable 
{
	/**  **/
	private static final long serialVersionUID = 1L;

	//主键id
	private String id;
	//泊位id
	private String berthId;
	//传感器地址
	private String sensorAddr;
	//传感器类型
	private Integer sensorType;
	//网络号
	private String netId;
	
	
	/**
	 * 传感器类型
	 */
	public static enum SensorType
	{
		RFSENSOR(1, "RF传感器"),
		NBSENSOR(2, "NB传感器"),
		LRSENSOR(3, "LR传感器");
		
		public int key;
		public String value;
		
		private SensorType(int key, String value)
		{
			this.key = key;
			this.value = value;
		}
		
		public static SensorType get(int key)
		{
			SensorType[] values = SensorType.values();
			for(SensorType object: values)
			{
				if(object.key == key)
				{
					return object;
				}
			}
			return null;
		}
	}
	

	/**
	 * @return id
	 */
	public String getId() 
	{
		return id;
	}
	

	/** 
	 * @param id
	 */
	public void setId(String id) 
	{
		this.id = id;
	}

	
	/**
	 * @return berthId
	 */
	public String getBerthId() 
	{
		return berthId;
	}

	
	/** 
	 * @param berthId
	 */
	public void setBerthId(String berthId) 
	{
		this.berthId = berthId;
	}

	
	/**
	 * @return sensorAddr
	 */
	public String getSensorAddr() 
	{
		return sensorAddr;
	}

	
	/** 
	 * @param sensorAddr
	 */
	public void setSensorAddr(String sensorAddr) 
	{
		this.sensorAddr = sensorAddr;
	}
	
	
	/**
	 * @return sensorType
	 */
	public Integer getSensorType() 
	{
		return sensorType;
	}

	
	/** 
	 * @param sensorType
	 */
	public void setSensorType(Integer sensorType) 
	{
		this.sensorType = sensorType;
	}
	

	/**
	 * @return netId
	 */
	public String getNetId() 
	{
		return netId;
	}

	
	/** 
	 * @param netId
	 */
	public void setNetId(String netId) 
	{
		this.netId = netId;
	}
}
