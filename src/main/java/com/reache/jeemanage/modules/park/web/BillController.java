/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.reache.jeemanage.common.config.Global;
import com.reache.jeemanage.common.persistence.Page;
import com.reache.jeemanage.common.utils.IdGen;
import com.reache.jeemanage.common.utils.StringUtils;
import com.reache.jeemanage.common.web.BaseController;
import com.reache.jeemanage.modules.park.Constant;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.service.ParkJiffyStandService;
import com.reache.jeemanage.modules.park.service.ParkOrderService;

import sun.misc.BASE64Encoder;

/**
 * 微信支付对账单下载
 * 
 * @author shockw
 * @version 2019-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/park/bill")
public class BillController extends BaseController {

	/**
	 * 查询微信支付对账单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("park:bill:view")
	@RequestMapping(value = { "list", "" })
	public String list(String billDate,HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println(billDate);
		//给billDate赋值，如果没有参数就使用当前日期
		if(billDate==null||"".equals(billDate)) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
			billDate = sdf.format(date);
		}
		// 向微信发起支付请求
		MyConfig config;
		List<List<String>> list = new ArrayList<List<String>>();
		try {
			config = new MyConfig();
			WXPay wxpay = new WXPay(config, null, true);
			Map<String, String> data = new HashMap<String, String>();
	        data.put("bill_date", billDate);
	        data.put("bill_type", "ALL");
	        Map<String, String> resp = wxpay.downloadBill(data);
	        String respData =  resp.get("data");
	        String[] rows = respData.split("\r\n");
	        for(int i =0;i<rows.length;i++) {
	        	String row = rows[i];
	        	String[] clomuns  = row.split(",");
	        	List<String> childList = new ArrayList<String>();
	        	for(int j=0;j<clomuns.length;j++) {
	        		childList.add(clomuns[j].replaceFirst("`", ""));
	        	}
	        	list.add(childList);
	        }
	        model.addAttribute("list", list);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "modules/park/billList";
	}

}