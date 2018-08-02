/**
 * created on 2018年6月19日 下午1:23:57
 */
package cn.utstarcom.rtc.netty.http.server.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import cn.utstarcom.rtc.common.RtcBeanNames;
import cn.utstarcom.rtc.config.RtcConfiguration;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author UTSC0167
 * @date 2018年6月19日
 *
 */
@Controller(RtcBeanNames.PRINTSWITCHACTION)
public class PrintSwitchAction extends AbstractHttpServerAction {

    private static final Logger log = LoggerFactory.getLogger(PrintSwitchAction.class);

    private static final String LOCALHOST_IP = "127.0.0.1";

    private static final String FIELD_IS_PRINT_URI = "isPrintUri";

    private static final String FIELD_IS_PRINT_DATA = "isPrintData";

    private static final String FLAG_TRUE = "true";

    private final RtcConfiguration rtcConfiguration;

    public PrintSwitchAction(RtcConfiguration rtcConfiguration) {
        this.rtcConfiguration = rtcConfiguration;
    }

    @Override
    public void doAction(ChannelHandlerContext ctx, FullHttpRequest request, String uri,
            String clientIp, int clientPort) {

        log.info("PrintSwitchAction doAction start for client ip: {} port: {} uri: {}", clientIp,
                clientPort, uri);

        if (!LOCALHOST_IP.equals(clientIp)) {
            log.info(
                    "PrintSwitchAction doAction for client ip: {} port: {} uri: {} the client ip don't equal LOCALHOST_IP: {} , don't can refresh rtc configuration.",
                    clientIp, clientPort, uri, LOCALHOST_IP);
            doResponse(request, ctx, HttpResponseStatus.FORBIDDEN);
        }

        final Map<String, List<String>> params = this.generateParams(uri);
        Optional<String> existingIsPrintUri = this.getParamValues(params, FIELD_IS_PRINT_URI);
        Optional<String> existingIsPrintData = this.getParamValues(params, FIELD_IS_PRINT_DATA);
        if (existingIsPrintUri.isPresent() || existingIsPrintData.isPresent()) {
            log.info(
                    "PrintSwitchAction doAction for client ip: {} port: {} uri: {} the request parmeters {} or {} is null.",
                    clientIp, clientPort, uri, FIELD_IS_PRINT_URI, FIELD_IS_PRINT_DATA);
        }

        boolean isPrintUri = false;
        boolean isPrintData = false;
        if (FLAG_TRUE.equalsIgnoreCase(existingIsPrintUri.get())) {
            isPrintUri = true;
        }
        if (FLAG_TRUE.equalsIgnoreCase(existingIsPrintData.get())) {
            isPrintData = true;
        }

        final boolean curIsPrintUri = rtcConfiguration.isCollectPrintUri();
        final boolean curIsPrintData = rtcConfiguration.isCollectPrintSentData();
        log.info(
                "PrintSwitchAction doAction for client ip: {} port: {} uri: {} the isPrintUri: {} isPrintData: {} curIsPrintUri: {} curIsPrintData: {}",
                clientIp, clientPort, uri, isPrintUri, isPrintData, curIsPrintUri, curIsPrintData);
        if (isPrintUri != curIsPrintUri) {
            rtcConfiguration.setCollectPrintUri(isPrintUri);
        }
        if (isPrintData != curIsPrintData) {
            rtcConfiguration.setCollectPrintSentData(isPrintData);
        }

        doResponse(request, ctx, HttpResponseStatus.OK);

        log.info("PrintSwitchAction doAction end for client ip: {} port: {}", clientIp, clientPort);

    }

}
