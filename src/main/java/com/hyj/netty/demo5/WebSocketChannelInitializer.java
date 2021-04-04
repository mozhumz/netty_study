package com.hyj.netty.demo5;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline=ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        //块写处理器
        pipeline.addLast(new ChunkedWriteHandler());
        //聚合处理器 将http msg聚合成一个完整的request或response
        pipeline.addLast(new HttpObjectAggregator(8192));
        //websocket处理器
        pipeline.addLast(new WebSocketServerProtocolHandler("/wsTest"));
        //最后一个handler（ChannelInboundHandler）用于处理接收到的消息
        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}
