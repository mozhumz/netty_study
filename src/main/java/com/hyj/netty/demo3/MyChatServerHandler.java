package com.hyj.netty.demo3;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取某个客户端的channel管道
        Channel channel = ctx.channel();
        //遍历管道组，将该客户端消息msg发送给所有客户端，实现群聊
        channelGroup.forEach(ch -> {
            if (channel == ch) {
                //如果是自己 则发送者显示自己
                ch.writeAndFlush("【自己】" + msg + "\n");
            } else {
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息:" + msg + "\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        Channel channel=ctx.channel();
//        channelGroup.writeAndFlush("【服务器】"+channel.remoteAddress()+"已加入\n");
//        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //通过通道组广播消息
        channelGroup.writeAndFlush("【服务器】" + channel.remoteAddress() + "已离开\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("【服务器】:" + channel.remoteAddress() + "已上线\n");
        channelGroup.writeAndFlush("【服务器】" + channel.remoteAddress() + "已加入\n");
        channelGroup.add(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("【服务器】:" + channel.remoteAddress() + "已下线\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }


}
