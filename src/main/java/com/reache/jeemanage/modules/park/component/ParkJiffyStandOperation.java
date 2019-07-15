package com.reache.jeemanage.modules.park.component;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class ParkJiffyStandOperation {
	public static final Object lock = new Object();
	public static CountDownLatch latch ;
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
			System.out.println("已连接车架："+NettyConfig.group);
			Iterator<Channel> iterator = NettyConfig.group.iterator();
			while (iterator.hasNext()) {
				try {
					ByteBuf pingMessage = Unpooled.buffer();
					pingMessage.writeBytes(req.getBytes());
					Channel channel = iterator.next();
					latch = new CountDownLatch(1);
					channel.writeAndFlush(pingMessage);
					latch.await(60,TimeUnit.SECONDS);
					System.out.println("车架已经操作完毕，请进入车架！");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("车架操作超时！");
					latch.countDown();
				}
			}
		}
	}
}
