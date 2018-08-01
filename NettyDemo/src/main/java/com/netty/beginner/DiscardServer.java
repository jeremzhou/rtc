package com.netty.beginner;

import javax.validation.spi.BootstrapState;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

	private int port;
	public DiscardServer(int port) {
		this.port = port;
	}
	public void run()throws Exception {
		EventLoopGroup boosGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b =new ServerBootstrap();
			b.group(boosGroup,workerGroup).channel(NioServerSocketChannel.class)
										.childHandler(new ChannelInitializer<SocketChannel>() {

											@Override
											protected void initChannel(SocketChannel ch) throws Exception {
												// TODO Auto-generated method stub
												ch.pipeline().addLast(new DiscardServerHandler());
											};
											
										}).option(ChannelOption.SO_BACKLOG, 128)
										.childOption(ChannelOption.SO_KEEPALIVE, true);
										ChannelFuture  f=b.bind(port).sync();
										f.channel().closeFuture().sync();
		} finally {
			//资源优雅释放
			boosGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 8088;
		try {
			new DiscardServer(port).run();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
