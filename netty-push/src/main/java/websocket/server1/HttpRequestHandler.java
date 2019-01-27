package websocket.server1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

//  扩展SimpleChannelInboundHandler 以处理FullHttpRequest 消息
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    //private static final File INDEX;

   //static {
   //    URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
   //    try {
   //        String path = location.toURI() + "websocket/index.html";
   //        path = !path.contains("file:") ? path : path.substring(5);
   //        INDEX = new File(path);
   //    } catch (URISyntaxException e) {
   //        throw new IllegalStateException("Unable to locate index.html", e);
   //    }
   //}

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,FullHttpRequest request) throws Exception {
        // 如果请求了WebSocket协议升级，则增加引用计数（调用retain()方法），并将它传递给下一个ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());//调用channelRead()方法完成之后，它将调用FullHttpRequest对象上的release()方法以释放它的资源。
        } else {
            // 处理100 Continue请求以符合HTTP1.1 规范
            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            //RandomAccessFile file = new RandomAccessFile(INDEX, "r"); // 读取index.html
            //HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
            //response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/plain; charset=UTF-8");
            //boolean keepAlive = HttpHeaders.isKeepAlive(request);
            //if (keepAlive) { // 如果请求了keep-alive，则添加所需要的HTTP头信息
            //    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            //    response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            //}
            //ctx.write(response);   // ❸将HttpResponse写到客户端
            //if (ctx.pipeline().get(SslHandler.class) == null) {   // ❹将index.html写到客户端
            //    ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            //} else {
            //    ctx.write(new ChunkedNioFile(file.getChannel()));
            //}
            // 写LastHttpContent并冲刷至客户端
            //ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            //if (!keepAlive) {   // ❻如果没有请求keep-alive，则在写操作完成后关闭Channel
            //    future.addListener(ChannelFutureListener.CLOSE);
            //}
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}