package cn.utstarcom.rtc.netty.http.server.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.common.IMyApplicationContext;

@Component
public class HttpServerActionMapImpl implements IHttpServerActionMap {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerActionMapImpl.class);

    private final static Map<String, IHttpServerAction> actionMap = new HashMap<String, IHttpServerAction>();

    @Autowired
    private IMyApplicationContext applicationContext;

    @Autowired
    private HttpUri httpUri;

    @PostConstruct
    public final void initAactionMap() {

        logger.info("initAactionMap begin to run.");
        Map<String, String> actionUriMap = this.httpUri.getActionUriMap();
        for (String uriReg : actionUriMap.keySet()) {
            String actionName = actionUriMap.get(uriReg);
            IHttpServerAction serverAction = getHttpServerAction(actionUriMap.get(uriReg));
            logger.info("initAactionMap uriReg: {} actionName: {} actionClass: {}", uriReg,
                    actionName, serverAction.getClass().getName());
            actionMap.put(actionName, serverAction);
        }
        logger.info("initAactionMap end to run. actionMap size: {}", actionUriMap.size());
    }

    /**
     * According to the action name get IHttpServerAction instance
     * 
     * @param IHttpServerAction
     *            httpServerAction name
     * 
     * @return the IHttpServerAction to which the specified key is mapped
     */
    @Override
    public final IHttpServerAction get(String uri) {
        return actionMap.get(getActionName(uri));
    }

    /**
     * according the http uri, get the matched httpServer action<br>
     * 
     * @param uri
     *            http request or request uri
     * @return the non-null value held by actionMap
     */
    private final String getActionName(String uri) {
        return httpUri.getActionName(uri);
    }

    private final IHttpServerAction getHttpServerAction(String beanName) {
        return (IHttpServerAction) applicationContext.getBean(beanName);
    }
}
