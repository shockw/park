/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.modules.park.entity.ParkIntfLog;
import com.reache.jeemanage.modules.park.dao.ParkIntfLogDao;

/**
 * 接口日志Service
 * @author qzfeng
 * @version 2019-08-02
 */
@Service
@Transactional(readOnly = true)
public class ParkIntfLogService extends CrudService<ParkIntfLogDao, ParkIntfLog> {

	public ParkIntfLog get(String id) {
		return super.get(id);
	}
	
	public List<ParkIntfLog> findList(ParkIntfLog parkIntfLog) {
		return super.findList(parkIntfLog);
	}
	
	public Page<ParkIntfLog> findPage(Page<ParkIntfLog> page, ParkIntfLog parkIntfLog) {
		return super.findPage(page, parkIntfLog);
	}
	
	@Transactional(readOnly = false)
	public void save(ParkIntfLog parkIntfLog) {
		super.save(parkIntfLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(ParkIntfLog parkIntfLog) {
		super.delete(parkIntfLog);
	}
	
}