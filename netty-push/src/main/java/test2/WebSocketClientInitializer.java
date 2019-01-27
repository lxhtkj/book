package test2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class WebSocketClientInitializer extends ChannelInitializer<Channel> {
    private SslContext sslContext;
    private boolean startTls;

    public WebSocketClientInitializer(SslContext sslContext, boolean startTls) {
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        ch.pipeline().addFirst(new SslHandler(sslEngine, startTls));
        ch.pipeline().addLast(new HttpClientCodec())
                .addLast(new HttpObjectAggregator(65535));
        //.addLast(new WebSocketClientProtocolHandler();



    }
}
