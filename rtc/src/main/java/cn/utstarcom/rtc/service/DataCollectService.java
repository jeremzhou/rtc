/**
 * created on 2018年6月8日 下午4:13:18
 */
package cn.utstarcom.rtc.service;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
public interface DataCollectService {

    HttpResponseStatus handle(Long recivedTime,String opt, String userId, String data);
}
