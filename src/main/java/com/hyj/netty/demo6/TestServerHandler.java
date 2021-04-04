package com.hyj.netty.demo6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
		if(msg.getDataType()==MyDataInfo.MyMessage.DataType.person){
			System.out.println(msg.getP().getName());
			System.out.println(msg.getP().getAddress());
			System.out.println(msg.getP().getAge());
		}else if(msg.getDataType()==MyDataInfo.MyMessage.DataType.dog){
			System.out.println(msg.getD().getName());
			System.out.println(msg.getD().getAddress());
			System.out.println(msg.getD().getAge());
		}else{
			System.out.println(msg.getC().getName());
			System.out.println(msg.getC().getAddress());
			System.out.println(msg.getC().getAge());
		}

	}
}
