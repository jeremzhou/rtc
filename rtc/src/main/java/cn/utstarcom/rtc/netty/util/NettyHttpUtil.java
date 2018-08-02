package cn.utstarcom.rtc.netty.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public final class NettyHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(NettyHttpUtil.class);

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private NettyHttpUtil() {
        // do nothing.
    }

    private static final String zeroIp = "0.0.0.0";
    private static final int zeroPort = 0;

    public final static String getHttpRemoteIp(final Channel channel, final HttpRequest request) {

        String clientIp = null;
        if (request != null)
            clientIp = request.headers().get("X-Forwarded-For");

        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
            if (inetSocketAddress == null)
                clientIp = zeroIp;
            else
                clientIp = inetSocketAddress.getAddress().getHostAddress();
        }

        return clientIp;
    }

    public final static int getHttpRemotePort(final Channel channel) {

        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        if (inetSocketAddress == null)
            return zeroPort;
        else
            return inetSocketAddress.getPort();
    }

    public final static String getHttpLocalIp(final Channel channel) {

        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.localAddress();
        if (inetSocketAddress == null)
            return zeroIp;
        else
            return inetSocketAddress.getAddress().getHostAddress();
    }

    public final static int getHttpLocalPort(final Channel channel) {

        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.localAddress();
        if (inetSocketAddress == null)
            return zeroPort;
        else
            return inetSocketAddress.getPort();
    }

    public final static Map<String, String> parseHttpParameters(ChannelHandlerContext ctx,
            FullHttpRequest request) {

        Map<String, String> paramMap = new HashMap<>();

        if (HttpMethod.GET == request.method()) {

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            queryStringDecoder.parameters().entrySet().forEach(entry -> {
                paramMap.put(entry.getKey(), entry.getValue().get(0));
            });
        } else if (HttpMethod.POST == request) {

            HttpPostRequestDecoder httpPostRequestDecoder = new HttpPostRequestDecoder(request);
            httpPostRequestDecoder.offer(request);

            List<InterfaceHttpData> bodyHttpDatas = httpPostRequestDecoder.getBodyHttpDatas();

            for (InterfaceHttpData interfaceHttpData : bodyHttpDatas) {

                Attribute attribute = (Attribute) interfaceHttpData;
                try {
                    paramMap.put(attribute.getName(), attribute.getValue());
                } catch (IOException e) {
                    logger.info("parseHttpParameters for attribute name: {} generate exception:",
                            attribute.getName());
                }
            }

        }

        return paramMap;
    }

}
