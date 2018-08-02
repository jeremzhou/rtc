package cn.utstarcom.rtc.netty.http.server;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.Task.StatisticalCollectResult;
import cn.utstarcom.rtc.common.LogToConsole;
import cn.utstarcom.rtc.config.RtcConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

@Component
// @Configuration
// @PropertySource(value =
// "file:${spring.property.path}/config/netty.properties")
public class HttpServerImpl implements IHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerImpl.class);

    private static final String OS_LINUX = "Linux";

    private static final int WAIT_RESULT_SLEEP_MILLISECONDS = 100;

    private final ChannelInitializer<SocketChannel> httpServerInitializer;

    private final RtcConfiguration rtcConfiguration;

    private final StatisticalCollectResult statisticalCollectResult;

    private Channel channel;

    public HttpServerImpl(ChannelInitializer<SocketChannel> httpServerInitializer,
            RtcConfiguration rtcConfiguration, StatisticalCollectResult statisticalCollectResult) {

        this.httpServerInitializer = httpServerInitializer;
        this.rtcConfiguration = rtcConfiguration;
        this.statisticalCollectResult = statisticalCollectResult;
    }

    @Override
    @PostConstruct
    public void start() {

        while (!statisticalCollectResult.isReady()) {

            logger.info("start because statisticalCollectResult isn't ready, sleep {} millisecond",
                    WAIT_RESULT_SLEEP_MILLISECONDS);
            try {
                TimeUnit.MILLISECONDS.sleep(WAIT_RESULT_SLEEP_MILLISECONDS);
            } catch (InterruptedException e) {
                logger.info(
                        "start because statisticalCollectResult isn't ready to sleep generate exception:",
                        e);
            }
        }
        new Thread(new NettyHttpServerThread(), "nettyHttpServerThread").start();
    }

    @Override
    @PreDestroy
    public void stop() {
        logger.info("netty http server beging to stop ...");
        if (this.channel != null) {
            logger.info("netty http server channel isn't null, so need close it.");
            this.channel.close().addListener(ChannelFutureListener.CLOSE);
        }
        logger.info("netty http server stop success!");
    }

    private class NettyHttpServerThread implements Runnable {

        @SuppressWarnings("unchecked")
        @Override
        public void run() {

            final String osType = System.getProperty("os.name");
            logger.info("netty http server on os: {} beging to start ...", osType);
            // Configure the server.
            EventLoopGroup bossGroup = null;
            EventLoopGroup workerGroup = null;
            @SuppressWarnings("rawtypes")
            Class channelClass = null;
            if (OS_LINUX.equals(osType)) {
                bossGroup = new EpollEventLoopGroup(rtcConfiguration.getBossGroupThreadNumbers());
                workerGroup = new EpollEventLoopGroup(
                        rtcConfiguration.getWorkerGroupThreadNumbers());
                channelClass = EpollServerSocketChannel.class;
            } else {
                bossGroup = new NioEventLoopGroup(rtcConfiguration.getBossGroupThreadNumbers());
                workerGroup = new NioEventLoopGroup(rtcConfiguration.getWorkerGroupThreadNumbers());
                channelClass = NioServerSocketChannel.class;
            }

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(channelClass)
                    .option(ChannelOption.SO_BACKLOG, rtcConfiguration.getChannelOptionSoBacklog())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                            rtcConfiguration.getChannelOptionConnectTimeoutMills())
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_RCVBUF, 1048576)
                    .childOption(ChannelOption.SO_SNDBUF, 1048576)
                    .handler(new LoggingHandler(rtcConfiguration.getHttpServerLoglevel()))
                    .childHandler(httpServerInitializer);

            try {
                channel = serverBootstrap.bind(rtcConfiguration.getHttpServerIp(),
                        rtcConfiguration.getHttpServerPort()).sync().channel();
                logger.info("netty http server bind: {} started success!",
                        "http://" + rtcConfiguration.getHttpServerIp() + ":"
                                + rtcConfiguration.getHttpServerPort());
                LogToConsole.out("HttpServerImpl",
                        "netty http server bind: " + "http://" + rtcConfiguration.getHttpServerIp()
                                + ":" + rtcConfiguration.getHttpServerPort() + " started success!");
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                logger.info("netty http server is starting， this process generated exception: ", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    }

}
