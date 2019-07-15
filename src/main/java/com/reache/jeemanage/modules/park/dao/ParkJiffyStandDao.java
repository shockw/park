/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.dao;

import com.reache.jeemanage.common.persistence.CrudDao;
import com.reache.jeemanage.common.persistence.annotation.MyBatisDao;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;

/**
 * 停车架DAO接口
 * @author shockw
 * @version 2019-02-24
 */
@MyBatisDao
public interface ParkJiffyStandDao extends CrudDao<ParkJiffyStand> {
	
}