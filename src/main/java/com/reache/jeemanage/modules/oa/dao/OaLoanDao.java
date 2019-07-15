/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.oa.dao;

import com.reache.jeemanage.common.persistence.CrudDao;
import com.reache.jeemanage.common.persistence.annotation.MyBatisDao;
import com.reache.jeemanage.modules.oa.entity.OaLoan;

/**
 * 借款流程DAO接口
 * @author shock
 * @version 2018-07-29
 */
@MyBatisDao
public interface OaLoanDao extends CrudDao<OaLoan> {
	public OaLoan getByProcInsId(String procInsId);
	public int updateInsId(OaLoan oaLoan);
	public int updateFinancialText(OaLoan oaLoan);
	public int updateLeadText(OaLoan oaLoan);
	public int updateMainLeadText(OaLoan oaLoan);
	public int updateTeller(OaLoan oaLoan);
}