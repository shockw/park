/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 业务操作日志Entity
 * @author qzfeng
 * @version 2019-08-03
 */
public class ParkBizoptLog extends DataEntity<ParkBizoptLog> {
	
	private static final long serialVersionUID = 1L;
	private String bizoptName;		// 操作名称
	private String orderId;		// 订单编号
	@Length(min=0, max=100, message="订单编号长度必须介于 0 和 100 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	private String sysUserId;		// 系统用户id
	private String sysUserLoginName;		// 系统用户登录名
	private Date bizoptTime;		// 操作时间
	private String bizoptContent;		// 操作内容
	private Date beginBizoptTime;		// 开始 操作时间
	private Date endBizoptTime;		// 结束 操作时间
	
	public ParkBizoptLog() {
		super();
	}

	public ParkBizoptLog(String id){
		super(id);
	}

	@Length(min=1, max=100, message="操作名称长度必须介于 1 和 100 之间")
	public String getBizoptName() {
		return bizoptName;
	}

	public void setBizoptName(String bizoptName) {
		this.bizoptName = bizoptName;
	}
	
	@Length(min=1, max=100, message="系统用户id长度必须介于 1 和 100 之间")
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	@Length(min=1, max=100, message="系统用户登录名长度必须介于 1 和 100 之间")
	public String getSysUserLoginName() {
		return sysUserLoginName;
	}

	public void setSysUserLoginName(String sysUserLoginName) {
		this.sysUserLoginName = sysUserLoginName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="操作时间不能为空")
	public Date getBizoptTime() {
		return bizoptTime;
	}

	public void setBizoptTime(Date bizoptTime) {
		this.bizoptTime = bizoptTime;
	}
	
	public String getBizoptContent() {
		return bizoptContent;
	}

	public void setBizoptContent(String bizoptContent) {
		this.bizoptContent = bizoptContent;
	}
	
	public Date getBeginBizoptTime() {
		return beginBizoptTime;
	}

	public void setBeginBizoptTime(Date beginBizoptTime) {
		this.beginBizoptTime = beginBizoptTime;
	}
	
	public Date getEndBizoptTime() {
		return endBizoptTime;
	}

	public void setEndBizoptTime(Date endBizoptTime) {
		this.endBizoptTime = endBizoptTime;
	}
		
}