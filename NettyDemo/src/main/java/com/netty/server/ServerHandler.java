package com.netty.server;

import com.netty.client.UserParam;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter{

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
//		接收客户端对象
		UserParam user = (UserParam)msg;
		System.out.println("客户端发来的消息：" + user.getId() + "," + user.getName() + "," + user.getRequestMessage());
		
//		给客户端返回对象
		UserData response = new UserData();
		response.setId(user.getId());
		response.setName("response" + user.getId());
		response.setResponseMessage("响应内容" + user.getId());
		
		ctx.writeAndFlush(response);
//		处理完毕，关闭服务端
		//ctx.addListener(ChannelFutureListener.CLOSE);

	}
	
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		// TODO Auto-generated method stub

	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
