package cn.utstarcom.rtc.netty.http.server.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import cn.utstarcom.rtc.common.RtcBeanNames;
import cn.utstarcom.rtc.common.RtcConstants;
import cn.utstarcom.rtc.config.RtcCache;
import cn.utstarcom.rtc.config.RtcConfiguration;
import cn.utstarcom.rtc.service.DataCollectService;
import cn.utstarcom.rtc.util.RtcUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

@Controller(RtcBeanNames.DATACOLLECTACTION)
public class DataCollectAction extends AbstractHttpServerAction {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectAction.class);

    private final DataCollectService dataCollectService;

    private final RtcConfiguration rtcConfiguration;

    public DataCollectAction(DataCollectService dataCollectService,
            RtcConfiguration rtcConfiguration) {

        this.dataCollectService = dataCollectService;
        this.rtcConfiguration = rtcConfiguration;
    }

    @Override
    public void doAction(ChannelHandlerContext ctx, FullHttpRequest request, String uri,
            String clientIp, int clientPort) {

        final long startTime = RtcUtil.getCurrentUnixTime();
        RtcCache.COLLECT_RESULT_MAP.get(startTime).getRecivedNums().incrementAndGet();
        logger.debug("DataCollectAction doAction start for client ip: {} port: {} uri: {}",
                clientIp, clientPort, uri);
        if (rtcConfiguration.isCollectPrintUri()) {
            logger.info("collect request-uri: {}", uri);
        }

        final Map<String, List<String>> params = this.generateParams(uri);
        final String opt = getParamterValue(ctx, request, uri, clientIp, clientPort, startTime,
                params, RtcConstants.FIELD_OPT);
        if (opt == null) {
            return;
        }

        // 处理南传BI的心跳消息
        final String name = getParamterValue(ctx, request, uri, clientIp, clientPort, startTime,
                params, RtcConstants.FIELD_NAME);
        if (name != null) {
            handleHeartbeat(ctx, request, uri, clientIp, clientPort, startTime, opt, name);
            return;
        }

        final String data = getParamterValue(ctx, request, uri, clientIp, clientPort, startTime,
                params, RtcConstants.FIELD_DATA);
        if (data == null) {
            return;
        }

        // 处理BI 1.0探针消息
        String userId = getParamterValue(ctx, request, uri, clientIp, clientPort, startTime, params,
                RtcConstants.FIELD_USER_ID);
        if (userId == null) {
            logger.debug(
                    "DataCollectAction doAction recived 1.0 version probe message, the user id is null.");
        }

        logger.debug("DataCollectAction doAction collected opt: {} userId: {} data: {}", opt,
                userId, data);
        HttpResponseStatus httpResponseStatus = dataCollectService.handle(startTime, opt, userId,
                data);
        if (userId == null) {
            if (HttpResponseStatus.OK.equals(httpResponseStatus))
                doResponse(request, ctx, RtcConstants.PROBE_VERSION_1_RETURN_PUT_OK);
            else
                doResponse(request, ctx, httpResponseStatus);
        } else
            doResponse(request, ctx, httpResponseStatus);
        logger.debug("DataCollectAction doAction end for client ip: {} port: {}", clientIp,
                clientPort);
    }

    private final String getParamterValue(ChannelHandlerContext ctx, FullHttpRequest request,
            String uri, String clientIp, int clientPort, long startTime,
            Map<String, List<String>> params, String parmaName) {

        final Optional<String> existingParam = this.getParamValues(params, parmaName);
        if (!existingParam.isPresent()) {

            if (RtcConstants.FIELD_OPT.equals(parmaName)
                    || RtcConstants.FIELD_DATA.equals(parmaName)) {
                RtcCache.COLLECT_RESULT_MAP.get(startTime).getErrorNums().incrementAndGet();
                logger.error(
                        "DataCollectAction doAction for client ip: {} port: {} uri: {} the parameter {} is null, return {}",
                        clientIp, clientPort, uri, parmaName, HttpResponseStatus.BAD_REQUEST);
                doResponse(request, ctx, HttpResponseStatus.BAD_REQUEST);
            }
            return null;
        }

        return existingParam.get();
    }

    private final void handleHeartbeat(ChannelHandlerContext ctx, FullHttpRequest request,
            String uri, String clientIp, int clientPort, long startTime, String optValue,
            String nameValue) {

        if (RtcConstants.FIELD_OPT_VALUE_GET.equals(optValue)
                && RtcConstants.FIELD_NAME_VALUE_EPGLOG1.equals(nameValue)) {
            logger.debug(
                    "DataCollectAction doAction recived correct nanchuan BI heartbeat message, return {}",
                    HttpResponseStatus.OK);
            doResponse(request, ctx, HttpResponseStatus.OK);
        } else {
            logger.warn(
                    "DataCollectAction doAction for client ip: {} port: {} uri: {} recived error nanchuan BI heartbeat message, return {}",
                    clientIp, clientPort, uri, HttpResponseStatus.BAD_REQUEST);
            doResponse(request, ctx, HttpResponseStatus.BAD_REQUEST);
        }
        RtcCache.COLLECT_RESULT_MAP.get(startTime).getRecivedNums().decrementAndGet();
    }
}
