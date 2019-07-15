/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.test.dao;

import com.reache.jeemanage.common.persistence.CrudDao;
import com.reache.jeemanage.common.persistence.annotation.MyBatisDao;
import com.reache.jeemanage.modules.test.entity.TestMemo;

/**
 * 单表生成1DAO接口
 * @author sss
 * @version 2018-05-07
 */
@MyBatisDao
public interface TestMemoDao extends CrudDao<TestMemo> {
	
}