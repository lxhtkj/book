package filetransport.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String SEPARATOR = System.getProperty("line.separator");

    /**
     * 读文件
     * RandomAccessFile file = new RandomAccessFile("C:/a.txt", "rw");
     *     FileChannel channel = file.getChannel();
     * 写文件
     * String content = "AAAAA";
     *     ByteBuffer writeBuffer = ByteBuffer.allocate(128);
     *     writeBuffer.put(content.getBytes());
     *     writeBuffer.flip();
     *     channel.write(buf);
     */
   @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg)throws Exception {
        File file = new File(msg);
        if (file.exists()) {
            if (!file.isFile()) {
                ctx.writeAndFlush("Not a file : " + file + SEPARATOR);
                return;
            }
            ctx.write(file + " " + file.length() + SEPARATOR);

            RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "rw");
            //FileChannel：文件通道，用于对文件进行读写操作；
            //Position：文件操作的指针位置，读取或者写入的起始点；
            //Count：操作的总字节数。
            FileRegion region = new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
            ctx.write(region);
            ctx.writeAndFlush(SEPARATOR);
            randomAccessFile.close();
        } else {
            ctx.writeAndFlush("File not found: " + file + SEPARATOR);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}