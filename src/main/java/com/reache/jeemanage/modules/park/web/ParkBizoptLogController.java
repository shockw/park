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
import com.reache.jeemanage.modules.park.entity.ParkBizoptLog;
import com.reache.jeemanage.modules.park.service.ParkBizoptLogService;

/**
 * 业务操作日志Controller
 * @author qzfeng
 * @version 2019-08-03
 */
@Controller
@RequestMapping(value = "${adminPath}/park/parkBizoptLog")
public class ParkBizoptLogController extends BaseController {

	@Autowired
	private ParkBizoptLogService parkBizoptLogService;
	
	@ModelAttribute
	public ParkBizoptLog get(@RequestParam(required=false) String id) {
		ParkBizoptLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = parkBizoptLogService.get(id);
		}
		if (entity == null){
			entity = new ParkBizoptLog();
		}
		return entity;
	}
	
	@RequiresPermissions("park:parkBizoptLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(ParkBizoptLog parkBizoptLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ParkBizoptLog> page = parkBizoptLogService.findPage(new Page<ParkBizoptLog>(request, response), parkBizoptLog); 
		model.addAttribute("page", page);
		return "modules/park/parkBizoptLogList";
	}

	@RequiresPermissions("park:parkBizoptLog:view")
	@RequestMapping(value = "form")
	public String form(ParkBizoptLog parkBizoptLog, Model model) {
		model.addAttribute("parkBizoptLog", parkBizoptLog);
		return "modules/park/parkBizoptLogForm";
	}
	
	@RequiresPermissions("park:parkBizoptLog:view")
	@RequestMapping(value = "formreadonly")
	public String formReadonly(ParkBizoptLog parkBizoptLog, Model model) {
		model.addAttribute("parkBizoptLog", parkBizoptLog);
		return "modules/park/parkBizoptLogFormReadonly";
	}

	@RequiresPermissions("park:parkBizoptLog:edit")
	@RequestMapping(value = "save")
	public String save(ParkBizoptLog parkBizoptLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, parkBizoptLog)){
			return form(parkBizoptLog, model);
		}
		parkBizoptLogService.save(parkBizoptLog);
		addMessage(redirectAttributes, "保存业务操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkBizoptLog/?repage";
	}
	
	@RequiresPermissions("park:parkBizoptLog:edit")
	@RequestMapping(value = "delete")
	public String delete(ParkBizoptLog parkBizoptLog, RedirectAttributes redirectAttributes) {
		parkBizoptLogService.delete(parkBizoptLog);
		addMessage(redirectAttributes, "删除业务操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkBizoptLog/?repage";
	}

}