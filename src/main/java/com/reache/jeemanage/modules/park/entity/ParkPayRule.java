/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.entity;

import org.hibernate.validator.constraints.Length;

import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 计费规则Entity
 * @author shock
 * @version 2018-11-20
 */
public class ParkPayRule extends DataEntity<ParkPayRule> {
	
	private static final long serialVersionUID = 1L;
	private String period;		// 计费周期，单位为分钟
	private String price;		// 单位周期内费用
	
	public ParkPayRule() {
		super();
	}

	public ParkPayRule(String id){
		super(id);
	}

	@Length(min=1, max=11, message="计费周期，单位为分钟长度必须介于 1 和 11 之间")
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	@Length(min=1, max=11, message="单位周期内费用长度必须介于 1 和 11 之间")
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}