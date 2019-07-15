/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.test.web;

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
import com.reache.jeemanage.modules.test.entity.TestMemo;
import com.reache.jeemanage.modules.test.service.TestMemoService;

/**
 * 单表生成1Controller
 * @author sss
 * @version 2018-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testMemo")
public class TestMemoController extends BaseController {

	@Autowired
	private TestMemoService testMemoService;
	
	@ModelAttribute
	public TestMemo get(@RequestParam(required=false) String id) {
		TestMemo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testMemoService.get(id);
		}
		if (entity == null){
			entity = new TestMemo();
		}
		return entity;
	}
	
	@RequiresPermissions("test:testMemo:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestMemo testMemo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TestMemo> page = testMemoService.findPage(new Page<TestMemo>(request, response), testMemo); 
		model.addAttribute("page", page);
		return "modules/test/testMemoList";
	}

	@RequiresPermissions("test:testMemo:view")
	@RequestMapping(value = "form")
	public String form(TestMemo testMemo, Model model) {
		model.addAttribute("testMemo", testMemo);
		return "modules/test/testMemoForm";
	}

	@RequiresPermissions("test:testMemo:edit")
	@RequestMapping(value = "save")
	public String save(TestMemo testMemo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, testMemo)){
			return form(testMemo, model);
		}
		testMemoService.save(testMemo);
		addMessage(redirectAttributes, "保存单表成功");
		return "redirect:"+Global.getAdminPath()+"/test/testMemo/?repage";
	}
	
	@RequiresPermissions("test:testMemo:edit")
	@RequestMapping(value = "delete")
	public String delete(TestMemo testMemo, RedirectAttributes redirectAttributes) {
		testMemoService.delete(testMemo);
		addMessage(redirectAttributes, "删除单表成功");
		return "redirect:"+Global.getAdminPath()+"/test/testMemo/?repage";
	}

}