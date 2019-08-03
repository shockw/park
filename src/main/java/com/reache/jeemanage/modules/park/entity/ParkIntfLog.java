/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 接口日志Entity
 * @author qzfeng
 * @version 2019-08-02
 */
public class ParkIntfLog extends DataEntity<ParkIntfLog> {
	
	private static final long serialVersionUID = 1L;
	private String intfName;		// 接口名
	private String callMethod;		// 调用方式
	private String orderId;		// 订单编号
	private String caller;		// 调用方
	private String callee;		// 被调用方
	private Date reqTime;		// 请求时间
	private Date rspTime;		// 响应时间
	private String reqMsg;		// 请求消息
	private String rspMsg;		// 响应消息
	private String callStatus;		// 调用状态
	private Date beginReqTime;		// 开始 请求时间
	private Date endReqTime;		// 结束 请求时间
	private Date beginRspTime;		// 开始 响应时间
	private Date endRspTime;		// 结束 响应时间
	
	public ParkIntfLog() {
		super();
	}

	public ParkIntfLog(String id){
		super(id);
	}

	@Length(min=1, max=32, message="接口名长度必须介于 1 和 32 之间")
	public String getIntfName() {
		return intfName;
	}

	public void setIntfName(String intfName) {
		this.intfName = intfName;
	}
	
	@Length(min=1, max=100, message="调用方式长度必须介于 1 和 100 之间")
	public String getCallMethod() {
		return callMethod;
	}

	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}
	
	@Length(min=0, max=64, message="订单编号长度必须介于 0 和 64 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=0, max=64, message="调用方长度必须介于 0 和 64 之间")
	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}
	
	@Length(min=0, max=64, message="被调用方长度必须介于 0 和 64 之间")
	public String getCallee() {
		return callee;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReqTime() {
		return reqTime;
	}

	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRspTime() {
		return rspTime;
	}

	public void setRspTime(Date rspTime) {
		this.rspTime = rspTime;
	}
	
	public String getReqMsg() {
		return reqMsg;
	}

	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}
	
	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	
	@Length(min=0, max=100, message="调用状态长度必须介于 0 和 100 之间")
	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	
	public Date getBeginReqTime() {
		return beginReqTime;
	}

	public void setBeginReqTime(Date beginReqTime) {
		this.beginReqTime = beginReqTime;
	}
	
	public Date getEndReqTime() {
		return endReqTime;
	}

	public void setEndReqTime(Date endReqTime) {
		this.endReqTime = endReqTime;
	}
		
	public Date getBeginRspTime() {
		return beginRspTime;
	}

	public void setBeginRspTime(Date beginRspTime) {
		this.beginRspTime = beginRspTime;
	}
	
	public Date getEndRspTime() {
		return endRspTime;
	}

	public void setEndRspTime(Date endRspTime) {
		this.endRspTime = endRspTime;
	}
		
}