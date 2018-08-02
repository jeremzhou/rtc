/**
 * created on 2017年9月6日 上午9:52:08
 */
package cn.utstarcom.rtc.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cn.utstarcom.rtc.bo.NeHeartbeatRequest;
import cn.utstarcom.rtc.bo.NeHeartbeatResponse;
import cn.utstarcom.rtc.common.RtcBeanNames;
import cn.utstarcom.rtc.manage.INeHeartbeatManage;
import cn.utstarcom.rtc.service.INeHeartbeatService;

/**
 * @author UTSC0167
 * @date 2017年9月6日
 *
 */
@Service(RtcBeanNames.NEHEARTBEATSERVICE)
public class NeHeartbeatServiceImpl implements INeHeartbeatService {

    private static final Logger logger = LoggerFactory.getLogger(NeHeartbeatServiceImpl.class);

    private static final Gson gson = new Gson();

    @Resource(name = RtcBeanNames.NEHEARTBEATMANAGE)
    private INeHeartbeatManage neHeartbeatManage;

    @Override
    public String doNeHeatbeat(String content) {

        logger.debug("doNeHeatbeat begin hanlde content: {}", content);

        NeHeartbeatRequest request = gson.fromJson(content, NeHeartbeatRequest.class);
        logger.debug("doNeHeatbeat from json to NeHeartbeatRequest: {}", request);

        int status = neHeartbeatManage.getNeStatus();

        NeHeartbeatResponse response = new NeHeartbeatResponse();
        response.setAppType(999);
        response.setAppVersion("1.0.0");
        response.setServiceStatus(status);
        logger.debug("doNeHeatbeat generate NeHeartbeatResponse: {}", response);

        String responseStr = gson.toJson(response);
        logger.debug("doNeHeatbeat from response to json: {}", responseStr);

        return responseStr;
    }

}
