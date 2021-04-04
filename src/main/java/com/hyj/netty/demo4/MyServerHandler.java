package com.hyj.netty.demo4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 针对服务器端，监听当前与某客户端的channel的超时事件
     * 假设 readerIdleTime < writerIdleTime < allIdleTime
     * 如果客户端没写，服务端没读，则为读空闲
     * 如果客户端在写，服务端没写，则为写空闲
     * {@link MyServerInitializer#initChannel(io.netty.channel.socket.SocketChannel)}
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent= (IdleStateEvent) evt;
            String evtName="";
            switch (idleStateEvent.state()){
                case READER_IDLE:
                    evtName="读空闲";
                    break;
                case WRITER_IDLE:
                    evtName="写空闲";
                    break;
                case ALL_IDLE:
                    evtName="读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"超时事件:"+evtName);
            ctx.channel().close();
        }
    }
}
