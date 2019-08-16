package com.reache.jeemanage.modules.park.component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reache.jeemanage.common.mapper.JsonMapper;
import com.reache.jeemanage.modules.park.api.ParkAPI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {  
	

    private String result;

      
    public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	/**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端连接开始...");
        NettyConfig.group.add(ctx.channel());
    }
 
    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端连接关闭...");
        NettyConfig.group.remove(ctx.channel());
    }
 
    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        System.out.println("信息接收完毕...");
    }
 
    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
 
    /**
     * 服务端处理客户端websocket请求的核心方法，这里接收了客户端发来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object info) throws Exception {
        System.out.println("接收到了："+info);
        ByteBuf buf = (ByteBuf) info;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        result = body;
        System.out.println("接收客户端数据:" + body);
        //出入库并行锁释放
        if(body.contains("200")&&body.contains("feedback")) {
        	ObjectMapper objectMapper = new ObjectMapper();
        	Map<String,String> map = objectMapper.readValue(body,HashMap.class);
        	String transId = map.get("transId");
        	CountDownLatch latch =ParkJiffyStandOperation.latchs.get(transId);
        	latch.countDown();
        	System.out.println("已经成功接受到车架操作完毕信息！");
        }else if(body.contains("barrier")) {
        	ObjectMapper objectMapper = new ObjectMapper();
        	Map<String,String> map = objectMapper.readValue(body,HashMap.class);
        	String state = map.get("state");
        	ParkAPI.barrierFlags.add(state);
        }
//        ByteBuf pingMessage = Unpooled.buffer();
//        pingMessage.writeBytes(req);
//        channelHandlerContext.writeAndFlush(pingMessage);
        
        //服务端使用这个就能向 每个连接上来的客户端群发消息
//        NettyConfig.group.writeAndFlush(info);
//        Iterator<Channel> iterator = NettyConfig.group.iterator();
//        while(iterator.hasNext()){
//        	ByteBuf pingMessage = Unpooled.buffer();
//        	pingMessage.writeBytes("return".getBytes());
//            iterator.next().writeAndFlush(pingMessage);
//        }
    }
    
    
}