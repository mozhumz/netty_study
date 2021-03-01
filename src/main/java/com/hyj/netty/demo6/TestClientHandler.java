package com.hyj.netty.demo6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
		System.out.println(msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//发送消息到服务端
		MyDataInfo.Person person=MyDataInfo.Person.newBuilder().setAddress("北京").setName("张三").setAge(55).build();
		MyDataInfo.MyMessage myMessage=null;
		int i=new Random().nextInt(3);
		if(i==0){
			myMessage=MyDataInfo.MyMessage.newBuilder()
					.setDataType(MyDataInfo.MyMessage.DataType.person)
					.setP(MyDataInfo.Person.newBuilder().setAddress("北京").setName("张三").setAge(55).build())
					.build();
		}else if(i==1){
			myMessage=MyDataInfo.MyMessage.newBuilder()
					.setDataType(MyDataInfo.MyMessage.DataType.dog)
					.setD(MyDataInfo.Dog.newBuilder().setAddress("北京-d").setName("一只狗").setAge(11).build())
					.build();
		}else {
			myMessage=MyDataInfo.MyMessage.newBuilder()
					.setDataType(MyDataInfo.MyMessage.DataType.cat)
					.setC(MyDataInfo.Cat.newBuilder().setAddress("北京-c").setName("一只猫").setAge(22).build())
					.build();
		}

		ctx.writeAndFlush(myMessage);
	}
}
