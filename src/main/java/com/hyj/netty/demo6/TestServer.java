package com.hyj.netty.demo6;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class TestServer {
	public static void main(String[] args) throws Exception{
		System.out.println("server start...");
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup wokerGroup=new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap=new ServerBootstrap();
			serverBootstrap.group(bossGroup,wokerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler()).childHandler(new TestServerInitializer());
			ChannelFuture channelFuture=serverBootstrap.bind(8899).sync();
			channelFuture.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully();
			wokerGroup.shutdownGracefully();
		}

	}
}
