package cn.utstarcom.rtc.netty.http.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.netty.http.server.action.IHttpServerActionMap;
import cn.utstarcom.rtc.netty.util.NettyHttpUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.unix.Errors.NativeIoException;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.timeout.ReadTimeoutException;

@Component
@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    @Autowired
    private IHttpServerActionMap httpServerActionMap;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            String uri = request.uri();
            String clientIp = NettyHttpUtil.getHttpRemoteIp(ctx.channel(), request);
            int clientPort = NettyHttpUtil.getHttpRemotePort(ctx.channel());
            logger.debug("handleHttp start for client ip: {} port: {} uri: {}", clientIp,
                    clientPort, uri);
            httpServerActionMap.get(uri).doAction(ctx, request, uri, clientIp, clientPort);
            logger.debug("handleHttp end for client ip: {} port: {} uri: {}", clientIp, clientPort,
                    uri);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        if (cause.getClass().equals(ReadTimeoutException.class)
                || cause.getClass().equals(NativeIoException.class))
            logger.debug(
                    "HttpServerHandler ReadTimeoutException for client ip: {} port: {} exception:",
                    NettyHttpUtil.getHttpRemoteIp(ctx.channel(), null),
                    NettyHttpUtil.getHttpRemotePort(ctx.channel()), cause);
        else
            logger.error(
                    "HttpServerHandler exceptionCaught class: {} for client ip: {} port: {} exception:",
                    cause.getClass().getName(), NettyHttpUtil.getHttpRemoteIp(ctx.channel(), null),
                    NettyHttpUtil.getHttpRemotePort(ctx.channel()), cause);

        ctx.close();
    }
}
