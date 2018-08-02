/**
 * created on 2018年1月25日 下午4:30:23
 */
package cn.utstarcom.rtc.netty.http.server.action;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.utstarcom.rtc.common.RtcBeanNames;

/**
 * @author UTSC0167
 * @date 2018年1月25日
 *
 */
public abstract class AbstractHttpUri implements HttpUri {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpUri.class);

    protected static final Map<String, String> actionUriMap = new LinkedHashMap<>();

    @PostConstruct
    public void initActionUriMap() {

        logger.info("initActionUriMap begin to run.");

        initPrivateActionUri();
        actionUriMap.put("^/NeHeartBeat$", RtcBeanNames.NEHEARTBEATACTION);
        actionUriMap.put(".*", RtcBeanNames.DATACOLLECTACTION);

        logger.info("initActionUriMap end to run. the actionUriMap size: {}", actionUriMap.size());

    }

    protected abstract void initPrivateActionUri();

    @Override
    public Map<String, String> getActionUriMap() {

        return actionUriMap;
    }

    @Override
    public String getActionName(String uri) {

        String actionName = RtcBeanNames.DEFAULTACTION;

        for (String uriReg : actionUriMap.keySet()) {
            if (uri.matches(uriReg))
                return actionUriMap.get(uriReg);
        }

        return actionName;
    }
}
