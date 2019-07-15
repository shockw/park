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
import com.reache.jeemanage.modules.park.entity.ParkPayRule;
import com.reache.jeemanage.modules.park.service.ParkPayRuleService;

/**
 * 计费规则Controller
 * @author shock
 * @version 2018-11-20
 */
@Controller
@RequestMapping(value = "${adminPath}/park/parkPayRule")
public class ParkPayRuleController extends BaseController {

	@Autowired
	private ParkPayRuleService parkPayRuleService;
	
	@ModelAttribute
	public ParkPayRule get(@RequestParam(required=false) String id) {
		ParkPayRule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = parkPayRuleService.get(id);
		}
		if (entity == null){
			entity = new ParkPayRule();
		}
		return entity;
	}
	
	@RequiresPermissions("park:parkPayRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(ParkPayRule parkPayRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ParkPayRule> page = parkPayRuleService.findPage(new Page<ParkPayRule>(request, response), parkPayRule); 
		model.addAttribute("page", page);
		return "modules/park/parkPayRuleList";
	}

	@RequiresPermissions("park:parkPayRule:view")
	@RequestMapping(value = "form")
	public String form(ParkPayRule parkPayRule, Model model) {
		model.addAttribute("parkPayRule", parkPayRule);
		return "modules/park/parkPayRuleForm";
	}

	@RequiresPermissions("park:parkPayRule:edit")
	@RequestMapping(value = "save")
	public String save(ParkPayRule parkPayRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, parkPayRule)){
			return form(parkPayRule, model);
		}
		ParkPayRule ppr =parkPayRuleService.get("1");
		if(ppr!=null) {
			parkPayRuleService.save(parkPayRule);
		}else {
			parkPayRule.setIsNewRecord(true);
			parkPayRuleService.save(parkPayRule);
		}
		
		addMessage(redirectAttributes, "保存计费规则成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkPayRule/form?id=1";
	}
	
	@RequiresPermissions("park:parkPayRule:edit")
	@RequestMapping(value = "delete")
	public String delete(ParkPayRule parkPayRule, RedirectAttributes redirectAttributes) {
		parkPayRuleService.delete(parkPayRule);
		addMessage(redirectAttributes, "删除计费规则成功");
		return "redirect:"+Global.getAdminPath()+"/park/parkPayRule/?repage";
	}

}