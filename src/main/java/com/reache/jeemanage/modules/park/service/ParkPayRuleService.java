/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.modules.park.entity.ParkPayRule;
import com.reache.jeemanage.modules.park.dao.ParkPayRuleDao;

/**
 * 计费规则Service
 * @author shock
 * @version 2018-11-20
 */
@Service
@Transactional(readOnly = true)
public class ParkPayRuleService extends CrudService<ParkPayRuleDao, ParkPayRule> {

	public ParkPayRule get(String id) {
		return super.get(id);
	}
	
	public List<ParkPayRule> findList(ParkPayRule parkPayRule) {
		return super.findList(parkPayRule);
	}
	
	public Page<ParkPayRule> findPage(Page<ParkPayRule> page, ParkPayRule parkPayRule) {
		return super.findPage(page, parkPayRule);
	}
	
	@Transactional(readOnly = false)
	public void save(ParkPayRule parkPayRule) {
		super.save(parkPayRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(ParkPayRule parkPayRule) {
		super.delete(parkPayRule);
	}
	
}