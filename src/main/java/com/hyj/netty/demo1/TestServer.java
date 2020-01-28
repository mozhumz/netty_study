package com.hyj.netty.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty接收http请求和处理
 */
public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("start...");
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new TestServerInitializer());
            ChannelFuture channelFuture=serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        }  finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
