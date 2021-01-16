package com.reache.jeemanage.modules.park.api;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

import com.reache.jeemanage.common.mapper.JsonMapper;
import com.reache.jeemanage.common.utils.FtpUtil;
import com.reache.jeemanage.common.utils.IdGen;
import com.reache.jeemanage.modules.park.Constant;
import com.reache.jeemanage.modules.park.TokenManager;
import com.reache.jeemanage.modules.park.component.NettyConfig;
import com.reache.jeemanage.modules.park.component.ParkJiffyStandOperation;
import com.reache.jeemanage.modules.park.component.WebSocketHandler;
import com.reache.jeemanage.modules.park.entity.ParkJiffyStand;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.entity.ParkPayRule;
import com.reache.jeemanage.modules.park.service.ParkJiffyStandService;
import com.reache.jeemanage.modules.park.service.ParkOrderService;
import com.reache.jeemanage.modules.park.service.ParkPayRuleService;
import com.reache.jeemanage.modules.sys.entity.User;

import sun.misc.BASE64Encoder;
import com.reache.jeemanage.modules.park.TokenManager;

@Controller
@RequestMapping(value = "${adminPath}/park/api")
public class ParkAPI {
	private static Logger logger = LoggerFactory.getLogger(ParkAPI.class);

	/**
	 * 操作人脸的同步锁
	 */
	public static Map<String, CountDownLatch> faceLatchs = new HashMap<String, CountDownLatch>();
	/**
	 * 查询车架下是否有人的共享变量
	 */
	public static List<String> barrierFlags = null;
	/**
	 * 待付款订单
	 */
	public static ParkOrder pendingOrder;
	@Autowired
	private ParkJiffyStandService parkJiffyStandService;
	@Autowired
	private ParkOrderService parkOrderService;
	@Autowired
	private ParkPayRuleService parkPayRuleService;
	@Autowired
	private WebSocketHandler webSocketHandler;

	// 下存车停单，触发人脸注册
	@RequestMapping(value = "/park")
	@ResponseBody
	public String park() {
		if (NettyConfig.group.isEmpty()) {
			audioPlay("audio/没有连接车架不能停车.wav");
			return Constant.ERROR_RESULT;
		}
		if (TokenManager.occupy()) {
			// 占用车位
			ParkJiffyStand pjs = parkJiffyStandService.occupy();
			// 如果占用车位成功，则开始人脸注册
			if (pjs != null) {
				String personId = IdGen.timdId8();
				// 人员注册,需要注册两次
				CloseableHttpClient httpclient = HttpClients.createDefault();
				try {
					// 入口人员注册
					HttpPost httpPost = new HttpPost(Constant.IN_URL + "/person/create");
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("pass", "88888888"));
					String personInfo = "{\"id\":\"" + personId + "\",\"idcardNum\":\"\",\"name\":\"" + personId
							+ "\"}";
					nvps.add(new BasicNameValuePair("person", personInfo));
					httpPost.setEntity(new UrlEncodedFormEntity(nvps));
					httpclient.execute(httpPost);
					// 出口人员注册
					HttpPost httpPostO = new HttpPost(Constant.OUT_URL + "/person/create");
					httpPostO.setHeader("Content-Type", "application/x-www-form-urlencoded");
					List<NameValuePair> nvpsO = new ArrayList<NameValuePair>();
					nvpsO.add(new BasicNameValuePair("pass", "88888888"));
					nvpsO.add(new BasicNameValuePair("person", personInfo));
					httpPostO.setEntity(new UrlEncodedFormEntity(nvpsO));
					httpclient.execute(httpPostO);
					// 入口门禁的人脸注册接口
					HttpPost httpPost1 = new HttpPost(Constant.IN_URL + "/face/takeImg");
					httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
					List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
					nvps1.add(new BasicNameValuePair("pass", "88888888"));
					nvps1.add(new BasicNameValuePair("personId", personId));
					httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
					httpclient.execute(httpPost1);
					// 形成订单
					ParkOrder po = new ParkOrder();
					po.setCreateBy(new User("1"));
					po.setUpdateBy(new User("1"));
					po.setCreateDate(new Date());
					po.setUpdateDate(new Date());
					po.setFloor(pjs.getFloor());
					po.setPersonId(personId);
					po.setJiffyStand(pjs.getJiffyStand());
					po.setStartTime(new Date());
					po.setStatus("0");
					parkOrderService.save(po);
					// 选择播放文件
					audioPlay("audio/占用车位成功.wav");
					ParkJiffyStandOperation.operation("in", personId, Integer.parseInt(po.getFloor()));
					CountDownLatch latch = new CountDownLatch(1);
					faceLatchs.put(personId, latch);
					FaceCallbackMonitor fcbm = new FaceCallbackMonitor(personId, latch);
					// 异步监控人脸是否回调成功,如果超时不回调，则释放令牌。
					Thread t = new Thread(fcbm);
					t.start();
					return Constant.SUCCESS_RESULT;
				} catch (Exception e) {
					// 选择播放文件
					audioPlay("audio/人员信息注册失败.wav");
					e.printStackTrace();
					TokenManager.release();
					return Constant.ERROR_RESULT;
				}
			} else {
				// 选择播放文件
				audioPlay("audio/车位已满.wav");
				TokenManager.release();
				return Constant.ERROR_RESULT;
			}
		} else {
			// 选择播放文件
			audioPlay("audio/他人在存取车.wav");
			return Constant.ERROR_RESULT;
		}
	}

	// 人脸注册拍照后进行回调，完成开门.
	@RequestMapping(value = "/order")
	@ResponseBody
	public String order(String personId, String newImgPath) {
		// 人脸回调登记
		CountDownLatch latch = faceLatchs.get(personId);
		latch.countDown();

		ParkOrder po = new ParkOrder();
		po.setPersonId(personId);
		List<ParkOrder> list = parkOrderService.findList(po);
		ParkOrder parkOrder = list.get(0);
		try {
			String base64code = "";
			CloseableHttpClient httpclient = HttpClients.createDefault();
			// 将照片同步到出口机器上
			FtpUtil.downloadFtpFile(Constant.FTP_IN, Constant.FTP_PORT, "/faceRegister", Constant.LOCAL_DIR,
					newImgPath.substring(newImgPath.indexOf("faceRegister") + 13));
			base64code = getImageStr(
					Constant.LOCAL_DIR + newImgPath.substring(newImgPath.indexOf("faceRegister") + 13));
			HttpPost httpPost1 = new HttpPost(Constant.OUT_URL + "/face/create");
			httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
			nvps1.add(new BasicNameValuePair("pass", "88888888"));
			nvps1.add(new BasicNameValuePair("personId", personId));
			nvps1.add(new BasicNameValuePair("faceId", ""));
			nvps1.add(new BasicNameValuePair("imgBase64", base64code));
			httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
			httpclient.execute(httpPost1);
			// 等待车架落地
			Thread.sleep(5000l);
			CountDownLatch countDownLatch = ParkJiffyStandOperation.latchs.get(personId);
			boolean b = countDownLatch.await(600, TimeUnit.SECONDS);
			ParkJiffyStandOperation.latchs.remove(personId);
			// 如果正常落架
			if (b == true) {
				// 开门
				HttpPost httpPost = new HttpPost(Constant.IN_URL + "/device/openDoorControl");
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("pass", "88888888"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				httpclient.execute(httpPost);
				System.out.println("-----门禁开门-----");
				// 更新订单状态为存车中，将拍照图片上传至数据库保存
				parkOrder.setPath(newImgPath);
				parkOrder.setInPic(base64code);
				parkOrder.setStatus("1");
				parkOrderService.save(parkOrder);
				// 判断用户是否存车成功，如果车架有人再变成没人，则存车成功，如果一直没人，固定时间段后默认存车成功，如果长时间一直有人，则向管理员告警
				barrierFlags = new ArrayList<String>();
				BarrierFlagMonitor hfm = new BarrierFlagMonitor(barrierFlags, personId);
				Thread t = new Thread(hfm);
				t.start();
			} else {
				// 没有正常落架，则视为异常，语音播报异常，释放令牌
				audioPlay("audio/车架操作异常.wav");
				TokenManager.release();
			}
		} catch (Exception e) {
			// 没有正常落架，则视为异常，语音播报异常，释放令牌
			audioPlay("audio/车架操作异常.wav");
			TokenManager.release();
			e.printStackTrace();
		}
		return Constant.SUCCESS_RESULT;
	}

	// 取车入门，只有人脸注册的人才可以通过人脸识别进入，接口给进门的人脸识备使用
	@RequestMapping(value = "/enter")
	@ResponseBody
	public String enter(String personId) {
		if (!"STRANGERBABY".equals(personId)) {
			if (TokenManager.occupy()) {
				ParkOrder po = new ParkOrder();
				po.setPersonId(personId);
				List<ParkOrder> list = parkOrderService.findList(po);
				ParkOrder parkOrder = list.get(0);
				// 车子落架
				ParkJiffyStandOperation.operation("out", personId, Integer.parseInt(parkOrder.getFloor()));
				try {
					// 等待车架落地
					CountDownLatch countDownLatch = ParkJiffyStandOperation.latchs.get(personId);
					boolean b = countDownLatch.await(600, TimeUnit.SECONDS);
					ParkJiffyStandOperation.latchs.remove(personId);
					if (b == true) {
						// 开门
						CloseableHttpClient httpclient = HttpClients.createDefault();
						HttpPost httpPost = new HttpPost(Constant.IN_URL + "/device/openDoorControl");
						httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						nvps.add(new BasicNameValuePair("pass", "88888888"));
						httpPost.setEntity(new UrlEncodedFormEntity(nvps));
						httpclient.execute(httpPost);
						// 更新订单状态为取车中
						parkOrder.setStatus("3");
						parkOrderService.save(parkOrder);
						// 判断用户是否存车成功，如果车架有人再变成没人，则存车成功，如果一直没人，固定时间段后默认存车成功，如果长时间一直有人，则向管理员告警
						barrierFlags = new ArrayList<String>();
						BarrierFlagMonitor hfm = new BarrierFlagMonitor(barrierFlags, personId);
						Thread t = new Thread(hfm);
						t.start();
					} else {
						// 没有正常落架，则视为异常，语音播报异常，释放令牌
						audioPlay("audio/车架操作异常.wav");
						TokenManager.release();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 选择播放文件
				audioPlay("audio/他人在存取车.wav");
			}
		} else {
			System.out.println("---识别为陌生人，不允许开门---");
			audioPlay("audio/陌生人禁止操作.wav");
		}
		return Constant.SUCCESS_RESULT;
	}

	// 出门通过人脸识别生成账单，接口给出门的人脸设备使用
	@RequestMapping(value = "/bill")
	@ResponseBody
	public String bill(String personId, String path, String type) {
		// 识别成功
		if ("face_0".equals(type)) {
			// 查找用户订单
			ParkOrder po = new ParkOrder();
			po.setPersonId(personId);
			List<ParkOrder> list = parkOrderService.findList(po);
			pendingOrder = list.get(0);
			if ("4".equals(pendingOrder.getStatus())) {
				// 费用计算
				ParkPayRule ppr = parkPayRuleService.get("1");
				String ss[] = path.split("/");
				FtpUtil.downloadFtpFile(Constant.FTP_OUT, Constant.FTP_PORT, "/recordsImg/" + ss[4], Constant.LOCAL_DIR,
						ss[5]);
				String base64code = getImageStr(Constant.LOCAL_DIR + ss[5]);
				pendingOrder.setOutPic(base64code);
				pendingOrder.setEndTime(new Date());
				long times = pendingOrder.getEndTime().getTime() - pendingOrder.getStartTime().getTime();
				pendingOrder.setCost(
						(int) (times / (Integer.valueOf(ppr.getPeriod()) * 60000) + 1) * Integer.valueOf(ppr.getPrice())
								+ "");
				parkOrderService.save(pendingOrder);
				audioPlay("audio/账单已生成请微信付款.wav");
				webSocketHandler.updateAndSendMsg(JsonMapper.toJsonString(pendingOrder));
				// 模拟付款
				try {
					Thread.sleep(5000l);
					// 查找待付款订单，如果没有，则不语音播报无付款订单，不需要付款
					if (ParkAPI.pendingOrder != null) {
						ParkOrder parkOrder = ParkAPI.pendingOrder;
						ParkAPI.pendingOrder = null;
						// 修改订单状态
						parkOrder.setStatus("5");
						parkOrder.setPayTime(new Date());
						parkOrderService.save(parkOrder);
						// 开门
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
						// 更新车架空闲车位数
						ParkJiffyStand parkJiffyStand = new ParkJiffyStand();
						parkJiffyStand.setFloor(parkOrder.getFloor());
						parkJiffyStand = parkJiffyStandService.findList(parkJiffyStand).get(0);
						int idleCount = Integer.valueOf(parkJiffyStand.getIdleCount()) + 1;
						int inuseCount = Integer.valueOf(parkJiffyStand.getInuseCount()) - 1;
						parkJiffyStand.setIdleCount(idleCount + "");
						parkJiffyStand.setInuseCount(inuseCount + "");
						parkJiffyStandService.save(parkJiffyStand);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("---没有取车，请离开----");
				audioPlay("audio/不取车请从侧门离开.wav");
			}
		} else {
			System.out.println("---人脸识别失败,无法生成账单----");
			audioPlay("audio/陌生人禁止操作.wav");
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

	public static void audioPlay(String audioPath) {
		// 创建audioclip对象
		AudioClip audioClip = null;
		try {
			String fileName = ParkAPI.class.getClassLoader().getResource(audioPath).getPath();
			// 选择播放文件
			File file = new File(fileName);
			// 将file转换为url
			audioClip = Applet.newAudioClip(file.toURL());
			// 循环播放 播放一次可以使用audioClip.play
			audioClip.play();
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//数据清理，包括门禁里面信息删除和令牌释放
	@RequestMapping(value = "/clean")
	@ResponseBody
	public String clean() {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost2 = new HttpPost(Constant.IN_URL + "/person/delete");
			httpPost2.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
			nvps2.add(new BasicNameValuePair("pass", "88888888"));
			nvps2.add(new BasicNameValuePair("id", "-1"));
			httpPost2.setEntity(new UrlEncodedFormEntity(nvps2));
			httpclient.execute(httpPost2);

			HttpPost httpPost1 = new HttpPost(Constant.OUT_URL + "/person/delete");
			httpPost1.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
			nvps1.add(new BasicNameValuePair("pass", "88888888"));
			nvps1.add(new BasicNameValuePair("id", "-1"));
			httpPost1.setEntity(new UrlEncodedFormEntity(nvps1));
			httpclient.execute(httpPost1);
			
			//令牌释放
			TokenManager.release();
			return Constant.SUCCESS_RESULT;
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.ERROR_RESULT;
		}

	}

	// 入口开门
	@RequestMapping(value = "/in")
	@ResponseBody
	public String in() {
		try {
			// 开门
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(Constant.IN_URL + "/device/openDoorControl");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("pass", "88888888"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpPost);
			return Constant.SUCCESS_RESULT;
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.ERROR_RESULT;
		}
	}

	// 出口开门
	@RequestMapping(value = "/out")
	@ResponseBody
	public String out() {
		try {
			// 开门
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(Constant.OUT_URL + "/device/openDoorControl");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("pass", "88888888"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpPost);
			return Constant.SUCCESS_RESULT;
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.ERROR_RESULT;
		}
	}

	// 出口开门
	@RequestMapping(value = "/orderClean")
	@ResponseBody
	public String orderClean() {
		try {
			// 开门
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(Constant.OUT_URL + "/device/openDoorControl");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("pass", "88888888"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpPost);
			return Constant.SUCCESS_RESULT;
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.ERROR_RESULT;
		}
	}

	public static void main(String[] args) {
		audioPlay("audio/他人在存取车.wav");
	}

}
