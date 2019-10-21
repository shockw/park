/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
import com.reache.jeemanage.modules.park.component.NettyConfig;
import com.reache.jeemanage.modules.park.component.ParkJiffyStandOperation;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.service.ParkJiffyStandService;
import com.reache.jeemanage.modules.park.service.ParkOrderService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import sun.misc.BASE64Encoder;

/**
 * 门禁Controller
 * 
 * @author shockw
 * @version 2019-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/park/doorAccess")
public class DoorController extends BaseController {

	/**
	 * 进入门禁控制页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "index", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/park/doorAccess";
	}

	/**
	 * 入口开门
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/inAccess")
	public String inAccess(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			// 开门
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(Constant.IN_URL + "/device/openDoorControl");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("pass", "88888888"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
		redirectAttributes.addFlashAttribute("message", "已开门!");
		return "redirect:" + Global.getAdminPath() + "/park/doorAccess/index";

	}
	
	/**
	 * 出口开门
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/outAccess")
	public String outAccess(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			// 开门
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(Constant.OUT_URL + "/device/openDoorControl");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("pass", "88888888"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
		redirectAttributes.addFlashAttribute("message", "已开门!");
		return "redirect:" + Global.getAdminPath() + "/park/doorAccess/index";

	}
	
	@RequestMapping(value = { "jiffyStandOper"})
	public String jiffyStandOper(ParkJiffyStand parkJiffyStand,HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("jiffyStandInfo", "车架连接信息：" + NettyConfig.group);
		return "modules/park/jiffyStandFallDown";
	}
	
	@RequestMapping(value = "/jiffyStandFallDown")
	public String jiffyStandFallDown(ParkJiffyStand parkJiffyStand, HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		int floor = Integer.valueOf(parkJiffyStand.getFloor());
		ParkJiffyStandOperation.operation("in", "manager_operation",floor);
		redirectAttributes.addFlashAttribute("message", "操作完成!");
		return "redirect:" + Global.getAdminPath() + "/park/doorAccess/jiffyStandOper";
	}
}