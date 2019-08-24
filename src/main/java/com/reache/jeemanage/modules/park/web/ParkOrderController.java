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
import com.reache.jeemanage.modules.park.TokenManager;
import com.reache.jeemanage.modules.park.api.ParkAPI;
import com.reache.jeemanage.modules.park.component.ParkJiffyStandOperation;
import com.reache.jeemanage.modules.park.component.WebSocketHandler;
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
	@Autowired
	private WebSocketHandler webSocketHandler;

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
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "index", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/park/index";
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
	@RequestMapping(value = { "history", "" })
	public String history(ParkOrder parkOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		parkOrder.setStatus("5");
		Page<ParkOrder> pop = new Page<ParkOrder>(request, response);
		Page<ParkOrder> page = parkOrderService.findPage(pop, parkOrder);
		model.addAttribute("page", page);
		return "modules/park/parkOrderHistory";
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
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = { "pay", "" })
	public String pay(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/park/parkPayForm";
	}

	// 扫码付款完成后回调此接口，开门
	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = { "bill", "" })
	public String pay(ParkOrder parkOrder) {
		String payCode = parkOrder.getPayCode();
		// 查找待付款订单，如果没有，则不语音播报无付款订单，不需要付款
		if (ParkAPI.pendingOrder != null) {
			parkOrder = ParkAPI.pendingOrder;
			parkOrder.setPayCode(payCode);
			ParkAPI.pendingOrder = null;
			// 向微信发起支付请求
			MyConfig config;
			try {
//				config = new MyConfig();
//				WXPay wxpay = new WXPay(config, null, true);
//				Map<String, String> reqData = new HashMap<String, String>();
//				reqData.put("device_info", "000");
//				reqData.put("body", "zongdian");
//				reqData.put("out_trade_no", parkOrder.getPersonId());
//				reqData.put("total_fee", parkOrder.getCost() + "00");
//				reqData.put("spbill_create_ip", "112.32.71.251");
//				reqData.put("auth_code", parkOrder.getPayCode());
//				Map<String, String> resData = wxpay.microPay(reqData);
//				System.out.println(resData);
				// 修改订单状态
				parkOrder.setStatus("5");
				parkOrder.setPayTime(new Date());
				parkOrderService.save(parkOrder);
				// 开门
//				CloseableHttpClient httpclient = HttpClients.createDefault();
//				HttpPost httpPost = new HttpPost(Constant.OUT_URL + "/device/openDoorControl");
//				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//				nvps.add(new BasicNameValuePair("pass", "88888888"));
//				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//				httpclient.execute(httpPost);
				// 删除入口设备和出口设备上的注册信息，
//				HttpPost httpPost2 = new HttpPost(Constant.IN_URL + "/person/delete");
//				httpPost2.setHeader("Content-Type", "application/x-www-form-urlencoded");
//				List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
//				nvps2.add(new BasicNameValuePair("pass", "88888888"));
//				nvps2.add(new BasicNameValuePair("id", parkOrder.getPersonId()));
//				httpPost2.setEntity(new UrlEncodedFormEntity(nvps2));
//				httpclient.execute(httpPost2);

//				HttpPost httpPost1 = new HttpPost(Constant.OUT_URL + "/person/delete");
//				httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
//				List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
//				nvps1.add(new BasicNameValuePair("pass", "88888888"));
//				nvps1.add(new BasicNameValuePair("id", parkOrder.getPersonId()));
//				httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
//				httpclient.execute(httpPost1);
				//更新车架空闲车位数
				ParkJiffyStand parkJiffyStand = new ParkJiffyStand();
				parkJiffyStand.setFloor(parkOrder.getFloor());
				parkJiffyStand = parkJiffyStandService.findList(parkJiffyStand).get(0);
				int idleCount = Integer.valueOf(parkJiffyStand.getIdleCount())+1;
				int inuseCount = Integer.valueOf(parkJiffyStand.getInuseCount())-1;
				parkJiffyStand.setIdleCount(idleCount+"");
				parkJiffyStand.setInuseCount(inuseCount+"");
				parkJiffyStandService.save(parkJiffyStand);
				//触发待付款页面信息更新
				webSocketHandler.updateAndSendMsg("closed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			// 选择播放文件
			ParkAPI.audioPlay("audio/无待付款订单不需要付款.wav");
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

	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = "tokenManager")
	public String tokenManager(Model model) {
		String statusDesc = TokenManager.getStatus() == true ? "可用" : "不可用";
		model.addAttribute("statusDesc", statusDesc);
		return "modules/park/tokenManager";
	}

	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = "tokenOccupy")
	public String tokenOccupy(RedirectAttributes redirectAttributes) {
		TokenManager.occupy();
		addMessage(redirectAttributes, "令牌占用成功");
		return "redirect:" + Global.getAdminPath() + "/park/parkOrder/tokenManager?repage";
	}

	@RequiresPermissions("park:parkOrder:edit")
	@RequestMapping(value = "tokenRelease")
	public String tokenRelease(RedirectAttributes redirectAttributes) {
		TokenManager.release();
		addMessage(redirectAttributes, "令牌释放成功");
		return "redirect:" + Global.getAdminPath() + "/park/parkOrder/tokenManager?repage";
	}

}