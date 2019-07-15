/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.dao;

import com.reache.jeemanage.common.persistence.CrudDao;
import com.reache.jeemanage.common.persistence.annotation.MyBatisDao;
import com.reache.jeemanage.modules.oa.entity.ServerAllocation;

/**
 * 服务器分配流程DAO接口
 * @author shock
 * @version 2018-05-15
 */
@MyBatisDao
public interface ServerAllocationDao extends CrudDao<ServerAllocation> {
	
}