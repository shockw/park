/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.service.CrudService;
import com.reache.jeemanage.common.utils.StringUtils;
import com.reache.jeemanage.modules.act.service.ActTaskService;
import com.reache.jeemanage.modules.act.utils.ActUtils;
import com.reache.jeemanage.modules.oa.entity.ServerAllocation;
import com.reache.jeemanage.modules.oa.dao.ServerAllocationDao;

/**
 * 服务器分配流程Service
 * @author shock
 * @version 2018-05-15
 */
@Service
@Transactional(readOnly = true)
public class ServerAllocationService extends CrudService<ServerAllocationDao, ServerAllocation> {
	@Autowired
	private ActTaskService actTaskService;

	public ServerAllocation get(String id) {
		return super.get(id);
	}
	
	public List<ServerAllocation> findList(ServerAllocation serverAllocation) {
		return super.findList(serverAllocation);
	}
	
	public Page<ServerAllocation> findPage(Page<ServerAllocation> page, ServerAllocation serverAllocation) {
		return super.findPage(page, serverAllocation);
	}
	
	@Transactional(readOnly = false)
	public void save(ServerAllocation serverAllocation) {
		// 申请发起
		if (StringUtils.isBlank(serverAllocation.getId())){
			serverAllocation.preInsert();
			dao.insert(serverAllocation);
			System.out.println(":::::::::::::"+serverAllocation);
			Map<String, Object> vars = Maps.newHashMap();
			System.out.println(":::::::::::::"+vars);
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_TEST_AUDIT[0], ActUtils.PD_TEST_AUDIT[1], serverAllocation.getId(), serverAllocation.getExplain(),vars);
			
		}
		
		// 重新编辑申请		
		else{
			serverAllocation.preUpdate();
			dao.update(serverAllocation);

			serverAllocation.getAct().setComment(("yes".equals(serverAllocation.getAct().getFlag())?"[重申] ":"[销毁] ")+serverAllocation.getAct().getComment());
			
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(serverAllocation.getAct().getFlag())? "1" : "0");
			System.out.println(":::::::::::::"+serverAllocation);
			actTaskService.complete(serverAllocation.getAct().getTaskId(), serverAllocation.getAct().getProcInsId(), serverAllocation.getAct().getComment(), serverAllocation.getExplain(), vars);
		}
		
		super.save(serverAllocation);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServerAllocation serverAllocation) {
		super.delete(serverAllocation);
	}
	
}