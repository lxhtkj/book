package test1.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(9999).start();// ←-- 调用服务器的start()方法
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup(); //←-- ❶ 创建EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap(); //←-- ❷ 创建ServerBootstrap
            //指定所使用的NIO传输Channel
            b.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(port) //使用指定的端口设置套接字地址  新链接建立时ChannelInitializer会将自定义的handler加入到子ChanelPipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() { //←-- ❺添加一个EchoServerHandler到子Channel的ChannelPipeline
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(serverHandler); //EchoServerHandler被标注为@Shareable， 所以我们可以总是使用同样的实例
                        }
                    });
            ChannelFuture f = b.bind().sync();// ←-- ❻ 异步地绑定服务器； 调用sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync();// ←-- ❼ 获取Channel的CloseFuture， 并且阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync();// ←-- ❽ 关闭EventLoopGroup， 释放所有的资源
        }
    }
}