package cn.utstarcom.rtc.netty.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.utstarcom.rtc.config.RtcConfiguration;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

@Configuration
public class NettyHttpConfigurationBean {

    @Bean(name = "httpServerInitializerEventExecutorGroup")
    public EventExecutorGroup getEventExecutorGroup(RtcConfiguration rtcConfiguration) {

        return new DefaultEventExecutorGroup(rtcConfiguration.getEventGroupThreadNumbers());
    }
}
