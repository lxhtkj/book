package test2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class CmdHandlerInitializer extends ChannelInitializer<Channel> {
    final static byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CmdDecoder(64 * 1024));  //   添加CmdDecoder 以提取Cmd 对象，并将它转发给下一个ChannelInboundHandler
        pipeline.addLast(new CmdHandler()); // 添加CmdHandler 以接收和处理Cmd 对象
    }

    public static final class Cmd {  // Cmd POJO
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf name() {
            return name;
        }

        public ByteBuf args() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
           ByteBuf buf = (ByteBuf) super.decode(ctx, buffer);
           if(buf == null){
               return null;
           }
           int index = buf.indexOf(buf.readerIndex(),buf.writerIndex(),SPACE);
           return new Cmd(buf.slice(buf.readerIndex(),index),buf.slice(index+1,buf.writerIndex()));
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Cmd msg)throws Exception {
             // Do something with the command  // 处理传经ChannelPipeline的Cmd 对象
        }
    }



}