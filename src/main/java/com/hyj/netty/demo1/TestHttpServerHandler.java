package com.hyj.netty.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 1
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler Added");
        super.handlerAdded(ctx);
    }

    /**
     * 2
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Registered");

        super.channelRegistered(ctx);
    }

    /**
     * 3
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Active");
        Channel channel=ctx.channel();
        System.out.println("channel:"+channel);
        super.channelActive(ctx);
    }


    /**
     * 4
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println(msg.getClass());
        System.out.println(ctx.channel().remoteAddress());
//        Thread.sleep(8000);
        if(msg instanceof HttpRequest){
            HttpRequest request= (HttpRequest) msg;
            System.out.println("请求方法名："+request.method().name());
            String uriStr=request.uri();
            URI uri=new URI(uriStr);
            String uriPath=uri.getPath();
            if("/favicon.ico".equals(uriPath)){
                System.out.println("请求:"+uriPath);
                return;
            }
            ByteBuf content= Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //信息返回给客户端
            ctx.writeAndFlush(response);
            ctx.channel().close();

        }
    }


    /**
     * 5
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Inactive");
        super.channelInactive(ctx);
    }

    /**
     * 6
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Unregistered");
        super.channelUnregistered(ctx);
    }

    /**
     * 7
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("handlerRemoved");
    }


}
