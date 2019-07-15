/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.web;

import java.util.List;

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
import com.reache.jeemanage.modules.oa.entity.OaLoan;
import com.reache.jeemanage.modules.oa.entity.TestAudit;
import com.reache.jeemanage.modules.oa.service.OaLoanService;
import com.reache.jeemanage.modules.sys.entity.Office;
import com.reache.jeemanage.modules.sys.utils.UserUtils;

/**
 * 借款流程Controller
 * 
 * @author shock
 * @version 2018-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaLoan")
public class OaLoanController extends BaseController {

	@Autowired
	private OaLoanService oaLoanService;

	@ModelAttribute
	public OaLoan get(@RequestParam(required = false) String id) {
		OaLoan entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = oaLoanService.get(id);
		}
		if (entity == null) {
			entity = new OaLoan();
		}
		return entity;
	}

	@RequiresPermissions("oa:oaLoan:view")
	@RequestMapping(value = { "list", "" })
	public String list(OaLoan oaLoan, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<OaLoan> page = oaLoanService.findPage(new Page<OaLoan>(request,
				response), oaLoan);
		model.addAttribute("page", page);
		return "modules/oa/oaLoanList";
	}

	@RequiresPermissions("oa:oaLoan:view")
	@RequestMapping(value = "form")
	public String form(OaLoan oaLoan, Model model) {
		if (oaLoan.getId() == null || "".equals(oaLoan.getId())) {
			oaLoan.setUser(UserUtils.getUser());
			oaLoan.setOffice(UserUtils.getUser().getOffice());
		}
		String view = "oaLoanForm";
		// 查看审批申请单
		if (StringUtils.isNotBlank(oaLoan.getId())) {
			// 环节编号
			String taskDefKey = oaLoan.getAct().getTaskDefKey();

			// 查看工单
			if (oaLoan.getAct().isFinishTask()) {
				view = "oaLoanView";
			}
			// 修改环节
			else if ("modify".equals(taskDefKey)) {
				view = "oaLoanForm";
			}
			// 审核环节
			else if ("audit".equals(taskDefKey)) {
				view = "oaLoanAudit";
			}
			// 审核环节2
			else if ("audit2".equals(taskDefKey)) {
				view = "oaLoanAudit";
			}
			// 审核环节3
			else if ("audit3".equals(taskDefKey)) {
				view = "oaLoanAudit";
			}
			// 审核环节4
			else if ("audit4".equals(taskDefKey)) {
				view = "oaLoanAudit";
			}
			// 兑现环节
			else if ("apply_end".equals(taskDefKey)) {
				view = "oaLoanAudit";
			}
		}
		model.addAttribute("oaLoan", oaLoan);
		return "modules/oa/" + view;
	}

	@RequiresPermissions("oa:oaLoan:edit")
	@RequestMapping(value = "save")
	public String save(OaLoan oaLoan, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaLoan)) {
			return form(oaLoan, model);
		}
		oaLoanService.save(oaLoan);
		addMessage(redirectAttributes, "提交借款流程'" + oaLoan.getUser().getName()
				+ "'成功");
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oa:oaLoan:edit")
	@RequestMapping(value = "saveAudit")
	public String saveAudit(OaLoan oaLoan, Model model) {
		if (StringUtils.isBlank(oaLoan.getAct().getFlag())
				|| StringUtils.isBlank(oaLoan.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(oaLoan, model);
		}
		oaLoanService.auditSave(oaLoan);
		return "redirect:" + adminPath + "/act/task/todo/";
	}

	@RequiresPermissions("oa:oaLoan:edit")
	@RequestMapping(value = "delete")
	public String delete(OaLoan oaLoan, RedirectAttributes redirectAttributes) {
		oaLoanService.delete(oaLoan);
		addMessage(redirectAttributes, "删除借款流程成功");
		return "redirect:" + Global.getAdminPath() + "/oa/oaLoan/?repage";
	}

}