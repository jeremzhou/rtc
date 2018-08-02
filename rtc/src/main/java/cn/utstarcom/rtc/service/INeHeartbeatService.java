/**
 * created on 2017年9月6日 上午9:39:35
 */
package cn.utstarcom.rtc.service;

/**
 * @author UTSC0167
 * @date 2017年9月6日
 *
 */
public interface INeHeartbeatService {
    
    /**
     * 处理网元心跳。
     * 
     * @param content 网元心跳请求内容
     * @return 返回网元心跳信息。
     */
    String doNeHeatbeat(String content);

}
