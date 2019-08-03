/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.modules.park.entity.ParkBizoptLog;
import com.reache.jeemanage.modules.park.dao.ParkBizoptLogDao;

/**
 * 业务操作日志Service
 * @author qzfeng
 * @version 2019-08-03
 */
@Service
@Transactional(readOnly = true)
public class ParkBizoptLogService extends CrudService<ParkBizoptLogDao, ParkBizoptLog> {

	public ParkBizoptLog get(String id) {
		return super.get(id);
	}
	
	public List<ParkBizoptLog> findList(ParkBizoptLog parkBizoptLog) {
		return super.findList(parkBizoptLog);
	}
	
	public Page<ParkBizoptLog> findPage(Page<ParkBizoptLog> page, ParkBizoptLog parkBizoptLog) {
		return super.findPage(page, parkBizoptLog);
	}
	
	@Transactional(readOnly = false)
	public void save(ParkBizoptLog parkBizoptLog) {
		super.save(parkBizoptLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(ParkBizoptLog parkBizoptLog) {
		super.delete(parkBizoptLog);
	}
	
}