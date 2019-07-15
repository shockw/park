/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.test.entity;

import org.hibernate.validator.constraints.Length;

import com.reache.jeemanage.common.persistence.DataEntity;

/**
 * 单表生成1Entity
 * @author sss
 * @version 2018-05-07
 */
public class TestMemo extends DataEntity<TestMemo> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String name;		// 名称
	private String content;		// 文章内容
	
	public TestMemo() {
		super();
	}

	public TestMemo(String id){
		super(id);
	}

	@Length(min=1, max=100, message="类型长度必须介于 1 和 100 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}