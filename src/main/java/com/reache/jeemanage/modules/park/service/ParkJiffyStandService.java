/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.modules.park.dao.ParkJiffyStandDao;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;

/**
 * 停车架Service
 * 
 * @author shockw
 * @version 2019-02-24
 */
@Service
@Transactional(readOnly = true)
public class ParkJiffyStandService extends CrudService<ParkJiffyStandDao, ParkJiffyStand> {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private static final Object lock = new Object();

	public ParkJiffyStand get(String id) {
		return super.get(id);
	}

	public List<ParkJiffyStand> findList(ParkJiffyStand parkJiffyStand) {
		return super.findList(parkJiffyStand);
	}

	public Page<ParkJiffyStand> findPage(Page<ParkJiffyStand> page, ParkJiffyStand parkJiffyStand) {
		return super.findPage(page, parkJiffyStand);
	}

	@Transactional(readOnly = false)
	public void save(ParkJiffyStand parkJiffyStand) {
		super.save(parkJiffyStand);
	}

	@Transactional(readOnly = false)
	public void delete(ParkJiffyStand parkJiffyStand) {
		super.delete(parkJiffyStand);
	}

	/**
	 * 车位占用
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public ParkJiffyStand occupy() {
		ParkJiffyStand pjs = null;
		synchronized (lock) {
			// 查找最新的存取车订单层数
			String sql1 = "select floor from park_order order by update_date desc";
			List<Map<String, Object>> lists1 = jdbcTemplate.queryForList(sql1);
			if (lists1.size() > 0) {
				// 获取最新存取车订单层数
				String new_floor = (String) lists1.get(0).get("floor");
				String sql = "select id,inuse_count,idle_count,floor,abs(floor-" + new_floor
						+ ") as distance,jiffy_stand from park_jiffy_stand where idle_count>0  order by distance";
				List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql);
				if (lists.size() > 0) {
					String id = (String) lists.get(0).get("id");
					String floor = (String) lists.get(0).get("floor");
					String jiffyStand = (String) lists.get(0).get("jiffy_stand");
					int inuseCount = (Integer) lists.get(0).get("inuse_count");
					int idleCount = (Integer) lists.get(0).get("idle_count");
					inuseCount++;
					idleCount--;
					String updateSql = "update park_jiffy_stand set inuse_count = " + inuseCount + ", idle_count = "
							+ idleCount + " where id = \"" + id + "\"";
					jdbcTemplate.update(updateSql);
					pjs = new ParkJiffyStand();
					pjs.setFloor(floor);
					pjs.setJiffyStand(jiffyStand);
					pjs.setId(id);
				}
			} else {
				String sql = "select id,inuse_count,idle_count,floor,jiffy_stand from park_jiffy_stand where idle_count>0  order by update_date desc";
				List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql);
				if (lists.size() > 0) {
					String id = (String) lists.get(0).get("id");
					String floor = (String) lists.get(0).get("floor");
					String jiffyStand = (String) lists.get(0).get("jiffy_stand");
					int inuseCount = (Integer) lists.get(0).get("inuse_count");
					int idleCount = (Integer) lists.get(0).get("idle_count");
					inuseCount++;
					idleCount--;
					String updateSql = "update park_jiffy_stand set inuse_count = " + inuseCount + ", idle_count = "
							+ idleCount + " where id = \"" + id + "\"";
					jdbcTemplate.update(updateSql);
					pjs = new ParkJiffyStand();
					pjs.setFloor(floor);
					pjs.setJiffyStand(jiffyStand);
					pjs.setId(id);
				}
			}
		}
		return pjs;
	}

}