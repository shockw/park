/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import com.reache.jeemanage.modules.sys.entity.Office;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reache.jeemanage.common.persistence.ActEntity;
import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 服务器分配流程Entity
 * @author shock
 * @version 2018-05-15
 */
public class ServerAllocation  extends ActEntity<ServerAllocation> {
	
	private static final long serialVersionUID = 1L;
	private String processInstanceId;		// 流程实例编号
	private Office office;		// 归属部门
	private String name;		// 名称
	private String explain;		// 说明
	private Date applyTime;		// 申请时间
	private Date expectedTime;		// 期望时间
	
	public ServerAllocation() {
		super();
	}

	public ServerAllocation(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=100, message="名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="说明长度必须介于 0 和 255 之间")
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(Date expectedTime) {
		this.expectedTime = expectedTime;
	}
	
}