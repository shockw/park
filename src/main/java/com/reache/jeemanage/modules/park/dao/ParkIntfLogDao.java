/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.dao;

import com.reache.jeemanage.common.persistence.CrudDao;
import com.reache.jeemanage.common.persistence.annotation.MyBatisDao;
import com.reache.jeemanage.modules.park.entity.ParkIntfLog;

/**
 * 接口日志DAO接口
 * @author qzfeng
 * @version 2019-08-02
 */
@MyBatisDao
public interface ParkIntfLogDao extends CrudDao<ParkIntfLog> {
	
}