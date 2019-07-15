/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import com.reache.jeemanage.modules.sys.entity.User;
import com.reache.jeemanage.modules.sys.entity.Office;
import com.reache.jeemanage.common.persistence.ActEntity;
import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 借款流程Entity
 * @author shock
 * @version 2018-07-29
 */
public class OaLoan extends  ActEntity<OaLoan>{
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private User user;		// 用户
	private Office office;		// 归属部门
	private String summary;		// 借款事由
	private String fee;		// 借款金额
	private String reason;		// 借款原因
	private String actbank;		// 开户行
	private String actno;		// 账号
	private String actname;		// 账号名
	private String financialText;		// 财务预审
	private String leadText;		// 部门领导意见
	private String mainLeadText;		// 总经理意见
	private String teller;		// 出纳支付
	
	public OaLoan() {
		super();
	}

	public OaLoan(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=64, message="借款事由长度必须介于 0 和 64 之间")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Length(min=0, max=11, message="借款金额长度必须介于 0 和 11 之间")
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	@Length(min=0, max=255, message="借款原因长度必须介于 0 和 255 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=255, message="开户行长度必须介于 0 和 255 之间")
	public String getActbank() {
		return actbank;
	}

	public void setActbank(String actbank) {
		this.actbank = actbank;
	}
	
	@Length(min=0, max=255, message="账号长度必须介于 0 和 255 之间")
	public String getActno() {
		return actno;
	}

	public void setActno(String actno) {
		this.actno = actno;
	}
	
	@Length(min=0, max=255, message="账号名长度必须介于 0 和 255 之间")
	public String getActname() {
		return actname;
	}

	public void setActname(String actname) {
		this.actname = actname;
	}
	
	@Length(min=0, max=255, message="财务预审长度必须介于 0 和 255 之间")
	public String getFinancialText() {
		return financialText;
	}

	public void setFinancialText(String financialText) {
		this.financialText = financialText;
	}
	
	@Length(min=0, max=255, message="部门领导意见长度必须介于 0 和 255 之间")
	public String getLeadText() {
		return leadText;
	}

	public void setLeadText(String leadText) {
		this.leadText = leadText;
	}
	
	@Length(min=0, max=255, message="总经理意见长度必须介于 0 和 255 之间")
	public String getMainLeadText() {
		return mainLeadText;
	}

	public void setMainLeadText(String mainLeadText) {
		this.mainLeadText = mainLeadText;
	}
	
	@Length(min=0, max=255, message="出纳支付长度必须介于 0 和 255 之间")
	public String getTeller() {
		return teller;
	}

	public void setTeller(String teller) {
		this.teller = teller;
	}
	
}