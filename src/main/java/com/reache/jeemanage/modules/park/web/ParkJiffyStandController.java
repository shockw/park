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
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;
import com.reache.jeemanage.modules.park.service.ParkJiffyStandService;

/**
 * 停车架Controller
 * @author shockw
 * @version 2019-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/park/parkJiffyStand")
public class ParkJiffyStandController extends BaseController {

	@Autowired
	private ParkJiffyStandService parkJiffyStandService;
	
	@ModelAttribute
	public ParkJiffyStand get(@RequestParam(required=false) String id) {
		ParkJiffyStand entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = parkJiffyStandService.get(id);
		}
		if (entity == null){
			entity = new ParkJiffyStand();
		}
		return entity;
	}
	
	@RequiresPermissions("park:parkJiffyStand:view")
	@RequestMapping(value = {"list", ""})
	public String list(ParkJiffyStand parkJiffyStand, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ParkJiffyStand> page = parkJiffyStandService.findPage(new Page<ParkJiffyStand>(request, response), parkJiffyStand); 
		model.addAttribute("page", page);
		return "modules/park/parkJiffyStandList";
	}

	@RequiresPermissions("park:parkJiffyStand:view")
	@RequestMapping(value = "form")
	public String form(ParkJiffyStand parkJiffyStand, Model model) {
		model.addAttribute("parkJiffyStand", parkJiffyStand);
		return "modules/park/parkJiffyStandForm";
	}

	@RequiresPermissions("park:parkJiffyStand:edit")
	@RequestMapping(value = "save")
	public String save(ParkJiffyStand parkJiffyStand, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, parkJiffyStand)){
			return form(parkJiffyStand, model);
		}
		parkJiffyStandService.save(parkJiffyStand);
		addMessage(redirectAttributes, "保存停车架成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkJiffyStand/?repage";
	}
	
	@RequiresPermissions("park:parkJiffyStand:edit")
	@RequestMapping(value = "delete")
	public String delete(ParkJiffyStand parkJiffyStand, RedirectAttributes redirectAttributes) {
		parkJiffyStandService.delete(parkJiffyStand);
		addMessage(redirectAttributes, "删除停车架成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkJiffyStand/?repage";
	}
	
	

}