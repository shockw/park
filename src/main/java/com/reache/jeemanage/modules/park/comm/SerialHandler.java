package com.reache.jeemanage.modules.park.comm;

import java.net.URI;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.reache.jeemanage.common.utils.SpringContextHolder;
import com.reache.jeemanage.modules.park.entity.ParkIntfLog;
import com.reache.jeemanage.modules.park.service.ParkIntfLogService;
import com.reache.jeemanage.modules.sys.entity.User;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialHandler {
	public void init() {
		System.out.println("打开串口，通过串口启动停车服务!");
		try {
			final SerialPort serialPort = SerialPortUtil.openSerialPort("COM4",9600);
			//设置串口的listener
			SerialPortUtil.setListenerToSerialPort(serialPort, new SerialPortEventListener() {

				@Override
				public void serialEvent(SerialPortEvent event) {
					System.out.println("开关触发");
					 //数据通知
	                if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	                    byte[] bytes = SerialPortUtil.readData(serialPort);
	                    System.out.println("从串口收到数据触发存车申请！数据长度："+bytes.length);
	                    ParkIntfLog parkIntfLog = new ParkIntfLog();
	            		parkIntfLog.setCreateDate(new Date());
	            		parkIntfLog.setCreateBy(new User("ab4636a2e21f408ebf7bb213dc24d206"));
	            		parkIntfLog.setUpdateBy(new User("ab4636a2e21f408ebf7bb213dc24d206"));
	            		parkIntfLog.setUpdateDate(new Date());
	            		parkIntfLog.setReqTime(new Date());
	            		parkIntfLog.setIntfName("接受串口信息");
	            		parkIntfLog.setCallMethod("2");
	            		parkIntfLog.setCallee("软件");
	            		parkIntfLog.setCaller("串口");
	            		parkIntfLog.setOrderId("串口接受信息");
	            		parkIntfLog.setCallStatus("0");
	            		parkIntfLog.setRspMsg(bytes.length+"");
	            		ParkIntfLogService parkIntfLogService = SpringContextHolder.getBean("parkIntfLogService");
	            		parkIntfLogService.save(parkIntfLog);
	                    try {
							CloseableHttpClient httpclient = HttpClients.createDefault();
							HttpGet httpGet = new HttpGet();
							httpGet.setURI(URI.create("http://localhost:8080/park/a/park/api/park"));
							HttpResponse response = httpclient.execute(httpGet);
							String result = EntityUtils.toString(response.getEntity(), "UTF-8");
							if(result.contains("true")) {
								System.out.println("开关触发：停车操作成功！");
							}else {
								System.out.println("开关触发：其他情况，暂时不能存车！");
							}
						} catch (Exception e) {
							System.out.println("开关触发：停车操作异常，请联系管理员！");
						} 
	                }
					
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		SerialHandler sh = new SerialHandler();
		sh.init();
	}
}
