/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.web;

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
import com.reache.jeemanage.modules.park.entity.ParkIntfLog;
import com.reache.jeemanage.modules.park.service.ParkIntfLogService;

/**
 * 接口日志Controller
 * @author qzfeng
 * @version 2019-08-02
 */
@Controller
@RequestMapping(value = "${adminPath}/park/parkIntfLog")
public class ParkIntfLogController extends BaseController {

	@Autowired
	private ParkIntfLogService parkIntfLogService;
	
	@ModelAttribute
	public ParkIntfLog get(@RequestParam(required=false) String id) {
		ParkIntfLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = parkIntfLogService.get(id);
		}
		if (entity == null){
			entity = new ParkIntfLog();
		}
		return entity;
	}
	
	@RequiresPermissions("park:parkIntfLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(ParkIntfLog parkIntfLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ParkIntfLog> page = parkIntfLogService.findPage(new Page<ParkIntfLog>(request, response), parkIntfLog); 
		model.addAttribute("page", page);
		return "modules/park/parkIntfLogList";
	}

	@RequiresPermissions("park:parkIntfLog:view")
	@RequestMapping(value = "form")
	public String form(ParkIntfLog parkIntfLog, Model model) {
		model.addAttribute("parkIntfLog", parkIntfLog);
		return "modules/park/parkIntfLogForm";
	}

	@RequiresPermissions("park:parkIntfLog:edit")
	@RequestMapping(value = "save")
	public String save(ParkIntfLog parkIntfLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, parkIntfLog)){
			return form(parkIntfLog, model);
		}
		parkIntfLogService.save(parkIntfLog);
		addMessage(redirectAttributes, "保存接口日志成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkIntfLog/?repage";
	}
	
	@RequiresPermissions("park:parkIntfLog:edit")
	@RequestMapping(value = "delete")
	public String delete(ParkIntfLog parkIntfLog, RedirectAttributes redirectAttributes) {
		parkIntfLogService.delete(parkIntfLog);
		addMessage(redirectAttributes, "删除接口日志成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkIntfLog/?repage";
	}

}