package test2;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

public class ChunkedWriteHandlerInitializer
        extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslCtx;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc())));   //  将SslHandler 添加到ChannelPipeline 中
        pipeline.addLast(new ChunkedWriteHandler()); //  添加ChunkedWriteHandler以处理作为ChunkedInput传入的数据
        pipeline.addLast(new WriteStreamHandler());   //  一旦连接建立，WriteStreamHandler就开始写文件数据
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        //  当连接建立时，channelActive()方法将使用ChunkedInput写文件数据
        @Override
        public void channelActive(ChannelHandlerContext ctx)throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}