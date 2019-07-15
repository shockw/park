/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.modules.test.entity.TestMemo;
import com.reache.jeemanage.modules.test.dao.TestMemoDao;

/**
 * 单表生成1Service
 * @author sss
 * @version 2018-05-07
 */
@Service
@Transactional(readOnly = true)
public class TestMemoService extends CrudService<TestMemoDao, TestMemo> {

	public TestMemo get(String id) {
		return super.get(id);
	}
	
	public List<TestMemo> findList(TestMemo testMemo) {
		return super.findList(testMemo);
	}
	
	public Page<TestMemo> findPage(Page<TestMemo> page, TestMemo testMemo) {
		return super.findPage(page, testMemo);
	}
	
	@Transactional(readOnly = false)
	public void save(TestMemo testMemo) {
		super.save(testMemo);
	}
	
	@Transactional(readOnly = false)
	public void delete(TestMemo testMemo) {
		super.delete(testMemo);
	}
	
}