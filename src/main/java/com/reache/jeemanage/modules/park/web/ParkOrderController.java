/**
 * Copyright &copy; 2012-2016 <a href="http://shockw.github.io/">JeeManage</a> All rights reserved.
 */
package com.reache.jeemanage.modules.park.web;

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
import com.reache.jeemanage.modules.park.component.ParkJiffyStandOperation;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.service.ParkJiffyStandService;
import com.reache.jeemanage.modules.park.service.ParkOrderService;

import sun.misc.BASE64Encoder;

/**
 * 停车订单Controller
 * 
 * @author shockw
 * @version 2019-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/park/parkOrder")
public class ParkOrderController extends BaseController {

	@Autowired
	private ParkOrderService parkOrderService;
	@Autowired
	private ParkJiffyStandService parkJiffyStandService;

	@ModelAttribute
	public ParkOrder get(@RequestParam(required = false) String id) {
		ParkOrder entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = parkOrderService.get(id);
		}
		if (entity == null) {
			entity = new ParkOrder();
		}
		return entity;
	}
	
	/**
	 * 进入存车操作页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "index", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/park/index";
	}
	
	/**
	 * 申请停车，形成订单，触发人脸注册
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/park")
	public String park(HttpServletRequest request, HttpServletResponse response, Model model,
				RedirectAttributes redirectAttributes) {
			try {
				// 占用车位
				ParkJiffyStand pjs = parkJiffyStandService.occupy();
				// 如果占用车位成功，则开始人脸注册
				if (pjs != null) {
					String personId = IdGen.uuid();
					//操作车架
					ParkJiffyStandOperation.operation("in", personId, Integer.parseInt(pjs.getFloor()));
					
					// 人员注册,需要注册两次
					CloseableHttpClient httpclient = HttpClients.createDefault();
					HttpPost httpPost = new HttpPost(Constant.IN_URL + "/person/create");
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("pass", "88888888"));
					String personInfo = "{\"id\":\"" + personId + "\",\"idcardNum\":\"\",\"name\":\"" + personId + "\"}";
					nvps.add(new BasicNameValuePair("person", personInfo));
					httpPost.setEntity(new UrlEncodedFormEntity(nvps));
					httpclient.execute(httpPost);

					HttpPost httpPostO = new HttpPost(Constant.OUT_URL + "/person/create");
					httpPostO.setHeader("Content-Type", "application/x-www-form-urlencoded");
					List<NameValuePair> nvpsO = new ArrayList<NameValuePair>();
					nvpsO.add(new BasicNameValuePair("pass", "88888888"));
					nvpsO.add(new BasicNameValuePair("person", personInfo));
					httpPostO.setEntity(new UrlEncodedFormEntity(nvpsO));
					httpclient.execute(httpPostO);
					// 照片注册,注册一次，另外一个需要照片同步
					
					HttpPost httpPost1 = new HttpPost(Constant.IN_URL + "/face/takeImg");
					httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
					List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
					nvps1.add(new BasicNameValuePair("pass", "88888888"));
					nvps1.add(new BasicNameValuePair("personId", personId));
					httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
					httpclient.execute(httpPost1);

					// 形成订单
					ParkOrder po = new ParkOrder();
					po.setFloor(pjs.getFloor());
					po.setPersonId(personId);
					po.setJiffyStand(pjs.getJiffyStand());
					po.setStartTime(new Date());
					po.setStatus("0");
					parkOrderService.save(po);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			redirectAttributes.addFlashAttribute("message", "正在申请停车,请对准摄像头进行拍照!");
			return "redirect:" + Global.getAdminPath() + "/park/parkOrder/index?repage";

		}
	
	@RequiresPermissions("park:parkOrder:view")
	@RequestMapping(value = { "list", "" })
	public String list(ParkOrder parkOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ParkOrder> pop = new Page<ParkOrder>(request, response);
		Page<ParkOrder> page = parkOrderService.findPage(pop, parkOrder);
		model.addAttribute("page", page);
		return "modules/park/parkOrderList";
	}

	@RequiresPermissions("park:parkOrder:view")
	@RequestMapping(value = "form")
	public String form(ParkOrder parkOrder, Model model) {
		model.addAttribute("parkOrder", parkOrder);
		return "modules/park/parkOrderForm";
	}

	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = "save")
	public String save(ParkOrder parkOrder, @RequestParam("file") MultipartFile file,
			@RequestParam("file1") MultipartFile file1, Model model, RedirectAttributes redirectAttributes) {
		try {
			BASE64Encoder encoder = new BASE64Encoder();
			String data = encoder.encode(file.getBytes());
			String data1 = encoder.encode(file1.getBytes());
			parkOrder.setInPic(data);
			parkOrder.setOutPic(data1);
			parkOrderService.save(parkOrder);
			addMessage(redirectAttributes, "保存停车订单成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + Global.getAdminPath() + "/park/parkOrder/list?repage";
	}
	/**
	 * 提供最近的待付款的账单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = { "pay", "" })
	public String pay(HttpServletRequest request, HttpServletResponse response, Model model) {
		ParkOrder parkOrder = new ParkOrder();
		Page<ParkOrder> pop = new Page<ParkOrder>(request, response);
		pop.setOrderBy("endTime asc");
		parkOrder.setStatus("3");
		Page<ParkOrder> page = parkOrderService.findPage(pop, parkOrder);
		List<ParkOrder> list = page.getList();
		if (list.size() > 0) {
			model.addAttribute("hasPay", "1");
			model.addAttribute("parkOrder", list.get(0));
		} else {
			model.addAttribute("hasPay", "0");
		}
		return "modules/park/parkPayForm";
	}

	// 扫码付款完成后回调此接口，开门
	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = { "bill", "" })
	public String pay(ParkOrder parkOrder) {
		// 向微信发起支付请求
		MyConfig config;
		try {
			config = new MyConfig();
			WXPay wxpay = new WXPay(config, null, true);
			Map<String, String> reqData = new HashMap<String, String>();
			reqData.put("device_info", "000");
			reqData.put("body", "zongdian");
			reqData.put("out_trade_no", parkOrder.getPersonId());
			reqData.put("total_fee", parkOrder.getCost()+"00");
			reqData.put("spbill_create_ip", "112.32.71.251");
			reqData.put("auth_code", parkOrder.getPayCode());
			Map<String, String> resData = wxpay.microPay(reqData);
			System.out.println(resData);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 修改订单状态
		parkOrder.setStatus("4");
		parkOrder.setPayTime(new Date());
		parkOrderService.save(parkOrder);
		
		try {
			// 开门
			Thread.sleep(60000l);
				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(Constant.OUT_URL + "/device/openDoorControl");
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("pass", "88888888"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				httpclient.execute(httpPost);
			// 删除入口设备和出口设备上的注册信息，
				HttpPost httpPost2 = new HttpPost(Constant.IN_URL + "/person/delete");
				httpPost2.setHeader("Content-Type", "application/x-www-form-urlencoded");
				List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
				nvps2.add(new BasicNameValuePair("pass", "88888888"));
				nvps2.add(new BasicNameValuePair("id", parkOrder.getPersonId()));
				httpPost2.setEntity(new UrlEncodedFormEntity(nvps2));
				httpclient.execute(httpPost2);

				HttpPost httpPost1 = new HttpPost(Constant.OUT_URL + "/person/delete");
				httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
				List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
				nvps1.add(new BasicNameValuePair("pass", "88888888"));
				nvps1.add(new BasicNameValuePair("id", parkOrder.getPersonId()));
				httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
				httpclient.execute(httpPost1);
		} catch (Exception e) {
		}
		return "redirect:" + Global.getAdminPath() + "/park/parkOrder/pay?repage";
	}

	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(ParkOrder parkOrder, RedirectAttributes redirectAttributes) {
		parkOrderService.delete(parkOrder);
		addMessage(redirectAttributes, "删除停车订单成功");
		return "redirect:" + Global.getAdminPath() + "/park/parkOrder/list?repage";
	}

}