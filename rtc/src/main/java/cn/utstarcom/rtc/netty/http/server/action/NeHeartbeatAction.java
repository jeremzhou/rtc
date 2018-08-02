package cn.utstarcom.rtc.netty.http.server.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import cn.utstarcom.rtc.common.RtcBeanNames;
import cn.utstarcom.rtc.netty.util.MediaType;
import cn.utstarcom.rtc.service.INeHeartbeatService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

@Controller(RtcBeanNames.NEHEARTBEATACTION)
public class NeHeartbeatAction extends AbstractHttpServerAction {

    private static final Logger logger = LoggerFactory.getLogger(NeHeartbeatAction.class);

    @Resource
    INeHeartbeatService neHeartbeatService;

    @Override
    public void doAction(ChannelHandlerContext ctx, FullHttpRequest request, String uri,
            String clientIp, int clientPort) {

        logger.debug("doAction NeHeartbeatAction start for client ip: {} port: {} uri: {}", clientIp,
                clientPort, uri);
        String content = this.getContent(request);
        logger.debug("doAction NeHeartbeatAction get content: {}", content);

        String responseJson = this.neHeartbeatService.doNeHeatbeat(content);
        logger.debug("doAction NeHeartbeatAction get responseJson: {}", responseJson);

        this.doResponse(request, ctx, responseJson, MediaType.APPLICATION_JSON_UTF8_VALUE);

        logger.debug("doAction NeHeartbeatAction end for client ip: {} port: {} uri: {}", clientIp,
                clientPort, uri);

    }

}
