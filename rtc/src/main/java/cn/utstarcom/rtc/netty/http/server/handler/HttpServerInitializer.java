package cn.utstarcom.rtc.netty.http.server.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.config.RtcConfiguration;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.EventExecutorGroup;

@Component
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final EventExecutorGroup eventExecutorGroup;

    private final SimpleChannelInboundHandler<?> httpServerHandler;

    private final RtcConfiguration rtcConfiguration;

    public HttpServerInitializer(
            @Qualifier("httpServerInitializerEventExecutorGroup") EventExecutorGroup eventExecutorGroup,
            SimpleChannelInboundHandler<?> httpServerHandler, RtcConfiguration rtcConfiguration) {

        this.eventExecutorGroup = eventExecutorGroup;
        this.httpServerHandler = httpServerHandler;
        this.rtcConfiguration = rtcConfiguration;
    }

    @Override
    public void initChannel(SocketChannel socketChannel) {

        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast("readTimeoutHandler",
                new ReadTimeoutHandler(rtcConfiguration.getReadTimeoutHandlerTimeoutSeconds()));
        channelPipeline.addLast("httpServerCodec", new HttpServerCodec());
        channelPipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(
                rtcConfiguration.getHttpObjectAggregatorMaxContentLength()));
        channelPipeline.addLast(eventExecutorGroup, "httpServerHandler", httpServerHandler);
    }
}
