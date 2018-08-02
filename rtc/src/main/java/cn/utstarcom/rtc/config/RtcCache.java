/**
 * created on 2018年6月8日 下午2:03:57
 */
package cn.utstarcom.rtc.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import cn.utstarcom.rtc.bo.CollectResult;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
public final class RtcCache {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private RtcCache() {
        // do nothing.
    }

    public static final Map<Long, CollectResult> COLLECT_RESULT_MAP = new ConcurrentHashMap<>();

    public static final Map<Integer, BlockingQueue<LinkedHashMap<String, String>>> HANDLED_DATA_QUEUE_MAP = new ConcurrentHashMap<>();
}
