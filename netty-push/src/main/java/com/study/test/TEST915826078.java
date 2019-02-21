package com.study.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class TEST915826078 {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TEST915826078 client = new TEST915826078();
        client.connect("www.baidu.com", 80);
    }

    /**
     * @param host
     * @param port
     * @throws Exception
     */
    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpResponseDecoder());//解码
                    ch.pipeline().addLast(new HttpRequestEncoder());//编码
                    ch.pipeline().addLast(new GetServerInfoHandler());//处理请求
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}



class GetServerInfoHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"https://www.baidu.com/", Unpooled.wrappedBuffer("".getBytes("UTF-8")));
        request.headers().set(HttpHeaderNames.HOST, "localhost");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            HttpHeaders header = response.headers();
            String value = "BWS/1.1";
            System.out.println("我的QQ号：915826078，我的解析到百度服务器server类型是：" + header.get(HttpHeaderNames.SERVER));
            assert  !value.equals(header.get(HttpHeaderNames.SERVER));
        }
        ctx.close();
    }
}