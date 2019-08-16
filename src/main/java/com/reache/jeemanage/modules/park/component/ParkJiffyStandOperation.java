package com.reache.jeemanage.modules.park.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.reache.jeemanage.modules.park.api.ParkAPI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class ParkJiffyStandOperation {
	public static final Object lock = new Object();
	/**
	 * 操作车架的同步锁
	 */
	public static Map<String,CountDownLatch> latchs = new HashMap<String,CountDownLatch>();
	
	
	
	/**
	 * 操作车架
	 * @param type
	 * @param userId
	 * @param floor
	 */
	public static void operation(String type, String userId, int floor) {
		synchronized (lock) {
			String req = "{\"cmd\":\"req\",\"type\":\"" + type + "\",\"transId\":\"" + userId +"\",\"userId\":\"" + userId + "\",\"floor\":" + floor
					+ "}";
			System.out.println("车架连接信息："+NettyConfig.group);
			Iterator<Channel> iterator = NettyConfig.group.iterator();
			while (iterator.hasNext()) {
				try {
					ByteBuf pingMessage = Unpooled.buffer();
					pingMessage.writeBytes(req.getBytes());
					Channel channel = iterator.next();
					CountDownLatch latch = new CountDownLatch(1);
					channel.writeAndFlush(pingMessage);
					latchs.put(userId, latch);
					System.out.println("向车架发送出入库操作成功！");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void barrierQuery(String transId) {
		String req = "{\"cmd\":\"req\",\"type\":\"barrier\",\"transId\":\"" + transId +"\"}";
		System.out.println("车架连接信息："+NettyConfig.group);
		Iterator<Channel> iterator = NettyConfig.group.iterator();
		while (iterator.hasNext()) {
			try {
				ByteBuf pingMessage = Unpooled.buffer();
				pingMessage.writeBytes(req.getBytes());
				Channel channel = iterator.next();
				channel.writeAndFlush(pingMessage);
				System.out.println("向车架发送障碍物查义命令成功！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
}
