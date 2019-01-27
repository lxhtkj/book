package test2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            //如果是客户端，则添加HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
            //如果是客户端，则添加HttpContentDecompressor 解压
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            //如果是服务器，则添加HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
            // 如果是服务器，则添加HttpContentCompressor来压缩数据（如果客户端支持它）
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }
}