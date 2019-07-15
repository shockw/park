/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.entity;

import org.hibernate.validator.constraints.Length;

import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 停车架Entity
 * @author shockw
 * @version 2019-02-24
 */
public class ParkJiffyStand extends DataEntity<ParkJiffyStand> {
	
	private static final long serialVersionUID = 1L;
	private String floor;		// 楼层
	private String jiffyStand;		// 停车架
	private String count;		// 总量
	private String inuseCount;		// 在用数量
	private String idleCount;		// 空闲数量
	private String number;		// number
	
	public ParkJiffyStand() {
		super();
	}

	public ParkJiffyStand(String id){
		super(id);
	}

	@Length(min=1, max=64, message="楼层长度必须介于 1 和 64 之间")
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	@Length(min=1, max=64, message="停车架长度必须介于 1 和 64 之间")
	public String getJiffyStand() {
		return jiffyStand;
	}

	public void setJiffyStand(String jiffyStand) {
		this.jiffyStand = jiffyStand;
	}
	
	@Length(min=1, max=11, message="总量长度必须介于 1 和 11 之间")
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	@Length(min=1, max=11, message="在用数量长度必须介于 1 和 11 之间")
	public String getInuseCount() {
		return inuseCount;
	}

	public void setInuseCount(String inuseCount) {
		this.inuseCount = inuseCount;
	}
	
	@Length(min=1, max=11, message="空闲数量长度必须介于 1 和 11 之间")
	public String getIdleCount() {
		return idleCount;
	}

	public void setIdleCount(String idleCount) {
		this.idleCount = idleCount;
	}
	
	@Length(min=1, max=64, message="number长度必须介于 1 和 64 之间")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}