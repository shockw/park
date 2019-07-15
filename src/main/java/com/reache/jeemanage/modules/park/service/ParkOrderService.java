/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.dao.ParkOrderDao;

/**
 * 停车订单Service
 * @author shockw
 * @version 2019-02-24
 */
@Service
@Transactional(readOnly = true)
public class ParkOrderService extends CrudService<ParkOrderDao, ParkOrder> {

	public ParkOrder get(String id) {
		return super.get(id);
	}
	
	public List<ParkOrder> findList(ParkOrder parkOrder) {
		return super.findList(parkOrder);
	}
	
	public Page<ParkOrder> findPage(Page<ParkOrder> page, ParkOrder parkOrder) {
		return super.findPage(page, parkOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(ParkOrder parkOrder) {
		super.save(parkOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(ParkOrder parkOrder) {
		super.delete(parkOrder);
	}
	
}