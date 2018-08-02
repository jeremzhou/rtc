/**
 * created on 2018年1月23日 下午4:05:55
 */
package cn.utstarcom.rtc.netty.http.server.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.common.RtcBeanNames;

/**
 * @author UTSC0167
 * @date 2018年1月23日
 *
 */
@Component
public class HttpUriImpl extends AbstractHttpUri {

    private static final Logger logger = LoggerFactory.getLogger(HttpUriImpl.class);

    @Override
    protected void initPrivateActionUri() {

        logger.info("initPrivateActionUri begin to run.");
        actionUriMap.put("^/print-switch?.*", RtcBeanNames.PRINTSWITCHACTION);
        logger.info("initPrivateActionUri end to run.");
    }

}
