package com.reache.jeemanage.modules.park.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reache.jeemanage.common.utils.FtpUtil;
import com.reache.jeemanage.modules.park.Constant;
import com.reache.jeemanage.modules.park.component.ParkJiffyStandOperation;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.entity.ParkPayRule;
import com.reache.jeemanage.modules.park.service.ParkJiffyStandService;
import com.reache.jeemanage.modules.park.service.ParkOrderService;
import com.reache.jeemanage.modules.park.service.ParkPayRuleService;

import sun.misc.BASE64Encoder;

@Controller
@RequestMapping(value = "${adminPath}/park/api")
public class ParkAPI {
	private static Logger logger = LoggerFactory.getLogger(ParkAPI.class);

	@Autowired
	private ParkJiffyStandService parkJiffyStandService;
	@Autowired
	private ParkOrderService parkOrderService;
	@Autowired
	private ParkPayRuleService parkPayRuleService;

	// 回调测试
	@RequestMapping(value = "/test")
	@ResponseBody
	public String test(String personId, String newImgPath) {
		System.out.println("回调测试：");
		return Constant.SUCCESS_RESULT;
	}

	// 人脸注册拍照后进行回调，完成开门.
	@RequestMapping(value = "/order")
	@ResponseBody
	public String order(String personId, String newImgPath) {
		System.out.println("--------");
		ParkOrder po = new ParkOrder();
		po.setPersonId(personId);
		List<ParkOrder> list = parkOrderService.findList(po);
		ParkOrder parkOrder = list.get(0);
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			// 将照片同步到出口机器上
			FtpUtil.downloadFtpFile(Constant.FTP_IP, Constant.FTP_PORT, "/faceRegister", Constant.LOCAL_DIR,
					newImgPath.substring(newImgPath.indexOf("faceRegister") + 13));
			String base64code = getImageStr(
					Constant.LOCAL_DIR + newImgPath.substring(newImgPath.indexOf("faceRegister") + 13));
			HttpPost httpPost1 = new HttpPost(Constant.OUT_URL + "/face/create");
			httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
			nvps1.add(new BasicNameValuePair("pass", "88888888"));
			nvps1.add(new BasicNameValuePair("personId", personId));
			nvps1.add(new BasicNameValuePair("faceId", ""));
			nvps1.add(new BasicNameValuePair("imgBase64", base64code));
			System.out.println(nvps1);
			httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
			httpclient.execute(httpPost1);
			// 开门
			HttpPost httpPost = new HttpPost(Constant.IN_URL + "/device/openDoorControl");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("pass", "88888888"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpPost);
			// 更新订单状态为已存车，将拍照图片上传至数据库保存
			parkOrder.setPath(newImgPath);
			parkOrder.setStatus("1");
			parkOrder.setInPic(base64code);
			parkOrderService.save(parkOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Constant.SUCCESS_RESULT;
	}

	// 取车入门，只有人脸注册的人才可以通过人脸识别进入，接口给进门的人脸识备使用
	@RequestMapping(value = "/enter")
	@ResponseBody
	public String enter(String personId) {
		System.out.println("--------");
		if (!"STRANGERBABY".equals(personId)) {
			ParkOrder po = new ParkOrder();
			po.setPersonId(personId);
			List<ParkOrder> list = parkOrderService.findList(po);
			ParkOrder parkOrder = list.get(0);
			// 车子落架
			ParkJiffyStandOperation.operation("out", personId, Integer.parseInt(parkOrder.getFloor()));
			System.out.println("车辆所在层已经放到地面，可以进行取车！");
			try {
				// 开门
				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(Constant.IN_URL + "/device/openDoorControl");
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("pass", "88888888"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				httpclient.execute(httpPost);
				// 更新订单状态为正在取车
				parkOrder.setStatus("2");
				parkOrderService.save(parkOrder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("---识别为陌生人，不允许开门---");
		}
		return Constant.SUCCESS_RESULT;
	}

	// 出门通过人脸识别生成账单，接口给出门的人脸设备使用
	@RequestMapping(value = "/bill")
	@ResponseBody
	public String bill(String personId, String path, String type) {
		// 识别成功
		if ("face_0".equals(type)) {
			// 费用记录
			System.out.println("---生成账单---");
			ParkPayRule ppr = parkPayRuleService.get("1");
			ParkOrder po = new ParkOrder();
			po.setPersonId(personId);
			List<ParkOrder> list = parkOrderService.findList(po);
			ParkOrder parkOrder = list.get(0);
			// 获取识别图片下载，并保存至数据库
			String ss[] = path.split("/");
			FtpUtil.downloadFtpFile(Constant.FTP_OUT, Constant.FTP_PORT, "/recordsImg/" + ss[4], Constant.LOCAL_DIR,
					ss[5]);
			String base64code = getImageStr(Constant.LOCAL_DIR + ss[5]);
			parkOrder.setOutPic(base64code);
			parkOrder.setEndTime(new Date());
			parkOrder.setStatus("3");
			long times = parkOrder.getEndTime().getTime() - parkOrder.getStartTime().getTime();
			parkOrder.setCost(
					(int) (times / (Integer.valueOf(ppr.getPeriod()) * 60000) + 1) * Integer.valueOf(ppr.getPrice())
							+ "");
			parkOrderService.save(parkOrder);
			// 停车架空闲车位数量变更
			ParkJiffyStand pjs = new ParkJiffyStand();
			pjs.setJiffyStand(parkOrder.getJiffyStand());
			pjs.setFloor(parkOrder.getFloor());
			List<ParkJiffyStand> pjsList = parkJiffyStandService.findList(pjs);
			pjs = pjsList.get(0);
			pjs.setIdleCount(Integer.parseInt(pjs.getIdleCount()) + 1 + "");
			pjs.setInuseCount(Integer.parseInt(pjs.getInuseCount()) - 1 + "");
			parkJiffyStandService.save(pjs);
		} else {
			System.out.println("---人脸识别失败,无法生成账单----");
		}
		return Constant.SUCCESS_RESULT;
	}

	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author:
	 * @CreateTime:
	 * @return
	 */
	private static String getImageStr(String imgFile) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

}
