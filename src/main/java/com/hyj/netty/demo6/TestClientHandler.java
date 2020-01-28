package com.hyj.netty.demo6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.Person> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.Person msg) throws Exception {

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MyDataInfo.Person person=MyDataInfo.Person.newBuilder().setAddress("北京").setName("张三").setAge(55).build();

		ctx.writeAndFlush(person);
	}
}
