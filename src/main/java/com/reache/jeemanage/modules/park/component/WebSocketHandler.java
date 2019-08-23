package com.reache.jeemanage.modules.park.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.reache.jeemanage.modules.park.entity.ParkOrder;

@Component
public class WebSocketHandler {
	@Autowired
    private SimpMessagingTemplate mesTemp;
	

	public void updateAndSendMsg(String message) {
        mesTemp.convertAndSend("/topic/bill", message);
    }
}
