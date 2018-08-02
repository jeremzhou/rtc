/**
 * created on 2018年1月23日 下午3:41:45
 */
package cn.utstarcom.rtc.netty.http.server.action;

import java.util.Map;

/**
 * @author UTSC0167
 * @date 2018年1月23日
 *
 */
public interface HttpUri {

    Map<String, String> getActionUriMap();
    
    String getActionName(String uri); 
}
