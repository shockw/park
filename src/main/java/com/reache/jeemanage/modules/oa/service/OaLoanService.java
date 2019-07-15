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
import com.reache.jeemanage.modules.oa.entity.OaLoan;
import com.reache.jeemanage.modules.oa.entity.TestAudit;
import com.reache.jeemanage.modules.oa.dao.OaLoanDao;
import com.reache.jeemanage.modules.sys.entity.Office;
import com.reache.jeemanage.modules.sys.entity.Role;
import com.reache.jeemanage.modules.sys.entity.User;
import com.reache.jeemanage.modules.sys.service.SystemService;

/**
 * 借款流程Service
 * 
 * @author shock
 * @version 2018-07-29
 */
@Service
@Transactional(readOnly = true)
public class OaLoanService extends CrudService<OaLoanDao, OaLoan> {

	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private SystemService systemService;

	public OaLoan get(String id) {
		return super.get(id);
	}

	public List<OaLoan> findList(OaLoan oaLoan) {
		return super.findList(oaLoan);
	}

	public Page<OaLoan> findPage(Page<OaLoan> page, OaLoan oaLoan) {
		return super.findPage(page, oaLoan);
	}

	@Transactional(readOnly = false)
	public void save(OaLoan oaLoan) {
		// 申请发起
		if (StringUtils.isBlank(oaLoan.getId())) {
			oaLoan.preInsert();
			dao.insert(oaLoan);
			System.out.println(":::::::::::::" + oaLoan);
			Map<String, Object> vars = Maps.newHashMap();
			// 启动流程
			actTaskService
					.startProcess(ActUtils.PD_OA_LOAN[0],
							ActUtils.PD_OA_LOAN[1], oaLoan.getId(),
							oaLoan.getSummary());

		}
		// 重新编辑申请
		else {
			oaLoan.preUpdate();
			dao.update(oaLoan);

			oaLoan.getAct().setComment(
					("yes".equals(oaLoan.getAct().getFlag()) ? "[重申] "
							: "[销毁] ") + oaLoan.getAct().getComment());

			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(oaLoan.getAct().getFlag()) ? "1"
					: "0");
			System.out.println(":::::::::::::" + oaLoan);
			actTaskService.complete(oaLoan.getAct().getTaskId(), oaLoan
					.getAct().getProcInsId(), oaLoan.getAct().getComment(),
					oaLoan.getSummary(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * 
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(OaLoan oaLoan) {
		Map<String, Object> vars = Maps.newHashMap();
		// 设置意见
		oaLoan.getAct().setComment(
				("yes".equals(oaLoan.getAct().getFlag()) ? "[同意] " : "[驳回] ")
						+ oaLoan.getAct().getComment());

		oaLoan.preUpdate();

		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaLoan.getAct().getTaskDefKey();

		// 审核环节
		if ("audit".equals(taskDefKey)) {
			oaLoan.setFinancialText(oaLoan.getAct().getComment());
			if("yes".equals(oaLoan.getAct().getFlag())){
				Office office = oaLoan.getOffice();
				List<User> userList = systemService.findUser(new User(new Role("5")));
				List<User> userList1 = systemService.findUserByOfficeId(office.getId());
				userList.retainAll(userList1);
				vars.put("dept_leader", userList.get(0).getLoginName());
			}
			dao.updateFinancialText(oaLoan);
		} else if ("audit2".equals(taskDefKey)) {
			oaLoan.setLeadText(oaLoan.getAct().getComment());
			dao.updateLeadText(oaLoan);
		} else if ("audit3".equals(taskDefKey)) {
			oaLoan.setMainLeadText(oaLoan.getAct().getComment());
			dao.updateMainLeadText(oaLoan);
		} else if ("apply_end".equals(taskDefKey)) {
			oaLoan.setTeller(oaLoan.getAct().getComment());
			dao.updateTeller(oaLoan);
		}

		// 未知环节，直接返回
		else {
			return;
		}

		// 提交流程任务
		vars.put("pass", "yes".equals(oaLoan.getAct().getFlag()) ? "1" : "0");
		actTaskService.complete(oaLoan.getAct().getTaskId(), oaLoan.getAct()
				.getProcInsId(), oaLoan.getAct().getComment(), vars);

	}

	@Transactional(readOnly = false)
	public void delete(OaLoan oaLoan) {
		super.delete(oaLoan);
	}

}