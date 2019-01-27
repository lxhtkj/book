package websocket.server1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

//  扩展SimpleChannelInboundHandler，并处理TextWebSocketFrame 消息
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,TextWebSocketFrame msg) throws Exception {
        // 增加消息的引用计数，并将它写到ChannelGroup 中所有已经连接的客户端
        //当channelRead0()方法返回时，TextWebSocketFrame的引用计数将会被减少。由于所有的操作都是异步的，
        // 因此，writeAnd-Flush()方法可能会在channelRead0()方法返回之后完成，而且它绝对不能访问一个已经失效的引用。
        group.writeAndFlush(msg.retain());
    }

    //重写userEventTriggered()方法以处理自定义事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            //  如果该事件表示握手成功，则从该Channelipeline中移除HttpRequestHandler，因为将不会接收到任何HTTP 消息了
            ctx.pipeline().remove(HttpRequestHandler.class);
            //通知所有已经连接的WebSocket 客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            // 将新的WebSocketChannel添加到ChannelGroup 中，以便它可以接收到所有的消息
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}