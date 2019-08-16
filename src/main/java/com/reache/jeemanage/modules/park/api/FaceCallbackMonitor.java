package com.reache.jeemanage.modules.park.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.reache.jeemanage.modules.park.TokenManager;

public class FaceCallbackMonitor implements Runnable{
	
	private String userId;
	
	private CountDownLatch countDownLatch;
	
	public FaceCallbackMonitor(String userId,CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
		this.userId = userId;
	}

	@Override
	public void run() {
		//5分钟不回调，则释放令牌
		try {
			boolean b = countDownLatch.await(300, TimeUnit.SECONDS);
			if(b==false) {
				TokenManager.release();
				System.out.println("由于回调超时，释放操作令牌！");
			}else {
				System.out.println("在正常时限内完成人脸注册回调！");
			}
			//删除并发锁
			ParkAPI.faceLatchs.remove(userId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

}
