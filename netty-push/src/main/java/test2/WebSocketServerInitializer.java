package test2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class WebSocketServerInitializer extends ChannelInitializer<Channel> {
    //要想为WebSocket添加安全性，只需要将SslHandler作为第一个ChannelHandler添加到ChannelPipeline中。
    private SslContext sslContext;
    public WebSocketServerInitializer(SslContext sslContext){
        this.sslContext = sslContext;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        ch.pipeline().addLast(
                new SslHandler(sslEngine),
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),  //  为握手提供聚合的HttpRequest
                new WebSocketServerProtocolHandler("/websocket"),//  如果被请求的端点是"/websocket"，则处理该升级握手
                new TextFrameHandler(),//  TextFrameHandler 处理TextWebSocketFrame
                new BinaryFrameHandler(),// BinaryFrameHandler 处理BinaryWebSocketFrame
                new ContinuationFrameHandler());//ContinuationFrameHandler 处理ContinuationWebSocketFrame
    }

    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,TextWebSocketFrame msg) throws Exception {}
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,BinaryWebSocketFrame msg) throws Exception {
        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,ContinuationWebSocketFrame msg) throws Exception {
        }
    }
}