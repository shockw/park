package com.reache.jeemanage.modules.park.comm;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
					 //数据通知
	                if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	                    byte[] bytes = SerialPortUtil.readData(serialPort);
	                    System.out.println("开关触发：从串口收到数据触发存车申请！数据长度："+bytes.length);
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
