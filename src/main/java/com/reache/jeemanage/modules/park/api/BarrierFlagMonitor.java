package com.reache.jeemanage.modules.park.api;

import java.util.List;
import java.util.UUID;

import com.reache.jeemanage.common.utils.SpringContextHolder;
import com.reache.jeemanage.modules.park.TokenManager;
import com.reache.jeemanage.modules.park.component.ParkJiffyStandOperation;
import com.reache.jeemanage.modules.park.entity.ParkOrder;
import com.reache.jeemanage.modules.park.service.ParkOrderService;

public class BarrierFlagMonitor implements Runnable{
	private List<String> barrierFlags ;
	private String userId;
	
	
	public BarrierFlagMonitor(List<String> barrierFlags,String userId) {
		this.barrierFlags = barrierFlags;
		this.userId = userId;
	}

	@Override
	public void run() {
		//每5秒向车架查询一次障碍物，10分钟超时
		for(int i =0;i<120;i++) {
			ParkJiffyStandOperation.barrierQuery(UUID.randomUUID().toString());
			try {
				Thread.sleep(5000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int flagCount = barrierFlags.size();
			if(flagCount>2&&"no".equals(barrierFlags.get(flagCount-1))&&barrierFlags.contains("has")) {
				//释放令牌
				TokenManager.release();
				ParkOrderService pos = SpringContextHolder.getBean("parkOrderService");
				ParkOrder po = new ParkOrder();
				po.setPersonId(userId);
				List<ParkOrder> list = pos.findList(po);
				ParkOrder parkOrder = list.get(0);
				if("1".equals(parkOrder.getStatus())) {
					parkOrder.setStatus("2");
				}else if ("3".equals(parkOrder.getStatus())) {
					parkOrder.setStatus("4");
				}
				pos.save(parkOrder);
				break;
			}else if(i>60&&!barrierFlags.contains("has")) {
				//释放令牌
				TokenManager.release();
				ParkOrderService pos = SpringContextHolder.getBean("parkOrderService");
				ParkOrder po = new ParkOrder();
				po.setPersonId(userId);
				List<ParkOrder> list = pos.findList(po);
				ParkOrder parkOrder = list.get(0);
				if("1".equals(parkOrder.getStatus())) {
					//修改订单状态为已存车
					parkOrder.setStatus("2");
				}else if ("3".equals(parkOrder.getStatus())) {
					//修改订单状态为已取车
					parkOrder.setStatus("4");
				}
				pos.save(parkOrder);
				break;
			}else if(i>110&&"has".equals(barrierFlags.get(flagCount-1))){
				//超长时间检测后仍然有人，并且语音告警，重复播报多次，直至循环结束，此时需要人工干预，释放令牌
				ParkAPI.audioPlay("audio/车架操作异常.wav");
			}
		}
		
	}

}
