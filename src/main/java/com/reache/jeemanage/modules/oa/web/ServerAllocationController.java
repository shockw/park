/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reache.jeemanage.common.config.Global;
import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.web.BaseController;
import com.reache.jeemanage.common.utils.StringUtils;
import com.reache.jeemanage.modules.oa.entity.ServerAllocation;
import com.reache.jeemanage.modules.oa.service.ServerAllocationService;

/**
 * 服务器分配流程Controller
 * @author shock
 * @version 2018-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/serverAllocation")
public class ServerAllocationController extends BaseController {

	@Autowired
	private ServerAllocationService serverAllocationService;
	
	@ModelAttribute
	public ServerAllocation get(@RequestParam(required=false) String id) {
		ServerAllocation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = serverAllocationService.get(id);
		}
		if (entity == null){
			entity = new ServerAllocation();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:serverAllocation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ServerAllocation serverAllocation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ServerAllocation> page = serverAllocationService.findPage(new Page<ServerAllocation>(request, response), serverAllocation); 
		model.addAttribute("page", page);
		return "modules/oa/serverAllocationList";
	}

	@RequiresPermissions("oa:serverAllocation:view")
	@RequestMapping(value = "form")
	public String form(ServerAllocation serverAllocation, Model model) {
		model.addAttribute("serverAllocation", serverAllocation);
		return "modules/oa/serverAllocationForm";
	}

	@RequiresPermissions("oa:serverAllocation:edit")
	@RequestMapping(value = "save")
	public String save(ServerAllocation serverAllocation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, serverAllocation)){
			return form(serverAllocation, model);
		}
		serverAllocationService.save(serverAllocation);
		addMessage(redirectAttributes, "保存服务器分配流程成功");
		return "redirect:"+Global.getAdminPath()+"/oa/serverAllocation/?repage";
	}
	
	@RequiresPermissions("oa:serverAllocation:edit")
	@RequestMapping(value = "delete")
	public String delete(ServerAllocation serverAllocation, RedirectAttributes redirectAttributes) {
		serverAllocationService.delete(serverAllocation);
		addMessage(redirectAttributes, "删除服务器分配流程成功");
		return "redirect:"+Global.getAdminPath()+"/oa/serverAllocation/?repage";
	}

}