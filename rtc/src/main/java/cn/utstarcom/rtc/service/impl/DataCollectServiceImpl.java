/**
 * created on 2018年6月8日 下午4:15:10
 */
package cn.utstarcom.rtc.service.impl;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.utstarcom.rtc.common.RtcConstants;
import cn.utstarcom.rtc.config.RtcCache;
import cn.utstarcom.rtc.config.RtcConfiguration;
import cn.utstarcom.rtc.service.DataCollectService;
import cn.utstarcom.rtc.util.RtcUtil;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
@Service
public class DataCollectServiceImpl implements DataCollectService {

    private static final Logger log = LoggerFactory.getLogger(DataCollectServiceImpl.class);

    private static final String ENCODING_ISO8859X_PREFIX = "iso-8859-";

    private static final String ENCODING_UTFX_PREFIX = "utf-";

    private static final String[] OPT_ENCODING_PREFIXS = { ENCODING_ISO8859X_PREFIX,
            ENCODING_UTFX_PREFIX };

    private static final String[] DATA_ENCODING_PREFIXS = { ENCODING_ISO8859X_PREFIX,
            ENCODING_UTFX_PREFIX };

    private final RtcConfiguration rtcConfiguration;

    public DataCollectServiceImpl(RtcConfiguration rtcConfiguration) {
        super();
        this.rtcConfiguration = rtcConfiguration;
    }

    @PostConstruct
    private void initHandledQueueMap() {

        final int rtcTopicNumPartitions = rtcConfiguration.getRtcTopicNumPartitions();
        log.info(
                "initHandledQueueMap begin. the HANDLED_DATA_QUEUE_MAP size: {} rtcTopicNumPartitions: {}",
                RtcCache.HANDLED_DATA_QUEUE_MAP.size(), rtcTopicNumPartitions);
        for (int i = 0; i < rtcTopicNumPartitions; i++) {
            BlockingQueue<LinkedHashMap<String, String>> dataQueue = new LinkedBlockingQueue<>();
            RtcCache.HANDLED_DATA_QUEUE_MAP.put(i, dataQueue);
        }
        log.info("initHandledQueueMap end. the HANDLED_DATA_QUEUE_MAP size: {}",
                RtcCache.HANDLED_DATA_QUEUE_MAP.size());

    }

    @Override
    public HttpResponseStatus handle(final Long recivedTime, String opt, String userId,
            String data) {

        if (!verifyEncoding(recivedTime, opt, userId, data)) {
            RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getErrorNums().incrementAndGet();
            log.error("handle for opt: {} userId: {} data: {} verifyEncoding false, return {}", opt,
                    userId, data, HttpResponseStatus.BAD_REQUEST);
            return HttpResponseStatus.BAD_REQUEST;
        }

        Optional<LinkedHashMap<String, String>> existingJsonDataMap = verifyData(recivedTime, data);
        if (!existingJsonDataMap.isPresent()) {
            RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getDiscardNums().incrementAndGet();
            log.error(
                    "handle for opt: {} userId: {} data: {} verifyData failed and will be discard, return {}",
                    opt, userId, data, HttpResponseStatus.BAD_REQUEST);
            return HttpResponseStatus.BAD_REQUEST;
        }

        LinkedHashMap<String, String> jsonDataMap = existingJsonDataMap.get();
        final String dataUserId = fixUserId(recivedTime, userId, jsonDataMap);
        if (dataUserId == null) {
            log.error(
                    "handle for opt: {} userId: {} data: {} after fixUserId the dataUserId is null, return {}",
                    opt, userId, data, HttpResponseStatus.BAD_REQUEST);
            RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getErrorNums().incrementAndGet();
            return HttpResponseStatus.BAD_REQUEST;
        }

        fixHttpsqsTime(recivedTime, jsonDataMap);
        fixFieldValue(recivedTime, jsonDataMap);
        final int partion = Math
                .abs(dataUserId.hashCode() % rtcConfiguration.getRtcTopicNumPartitions());
        log.debug("handle for userId: {} dataUserId: {} use kafka partion: {}", userId, dataUserId,
                partion);
        RtcCache.HANDLED_DATA_QUEUE_MAP.get(partion).add(jsonDataMap);
        RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getHandledNums().incrementAndGet();

        return HttpResponseStatus.OK;
    }

    private final boolean verifyEncoding(Long recivedTime, String opt, String userId, String data) {

        if (!isCorrectEncoding(opt, OPT_ENCODING_PREFIXS)) {

            log.error("verifyEncoding the parameter opt: {} encoding don't correct, return false",
                    opt);
            return false;
        }

        if (userId != null && !isCorrectEncoding(userId, OPT_ENCODING_PREFIXS)) {

            log.error(
                    "verifyEncoding the parameter userId: {} encoding don't correct, return false",
                    userId);
            return false;
        }

        if (!isCorrectEncoding(data, DATA_ENCODING_PREFIXS)) {

            log.error("verifyEncoding the parameter data: {} encoding don't correct, return false",
                    data);
            return false;
        }

        return true;
    }

    private final boolean isCorrectEncoding(String str, String[] encoding_prefixs) {

        final String encoding = RtcUtil.tryEncoding(str);
        log.debug("isCorrectEncoding the str: {} encoding: {}", str, encoding);
        for (String encoding_prefix : encoding_prefixs) {

            if (encoding.toLowerCase().startsWith(encoding_prefix))
                return true;
        }

        log.error(
                "isCorrectEncoding for str: {} the encoding: {} dont't match encoding_prefixs: {}",
                str, encoding, encoding_prefixs);
        return false;
    }

    private final Optional<LinkedHashMap<String, String>> verifyData(final Long recivedTime,
            String data) {

        Optional<LinkedHashMap<String, String>> existingJsonDataMap = Optional.empty();

        if (data.contains("<html>") || !data.endsWith("}")) {
            log.error("verifyData the data: {} contains <html> or not end with } so return empty",
                    data);
            return existingJsonDataMap;
        }

        boolean fixFlag = false;
        final StringBuilder sbData = new StringBuilder(data);
        if (data.endsWith(RtcConstants.FIX_QUESTION_MARK)) {
            RtcUtil.replace(sbData, RtcConstants.FIX_QUESTION_MARK, RtcConstants.EMPTY_STRING);
            fixFlag = true;
            log.warn("verifyData the data: {} endWith {}, fix it to empty string", data,
                    RtcConstants.FIX_QUESTION_MARK);
        }

        if (data.contains("<script>")) {
            if (RtcUtil.replace(sbData, RtcConstants.FIX_START_SCRIPT, RtcConstants.FIX_END_SCRIPT,
                    RtcConstants.EMPTY_STRING) == null
                    || RtcUtil.replace(sbData, " " + RtcConstants.FIX_START_SCRIPT,
                            RtcConstants.FIX_END_SCRIPT + " ", RtcConstants.EMPTY_STRING) == null) {
                log.error(
                        "verifyData the data: {} contains <script> to fix it to empty string fail, so return empty",
                        data);
                return existingJsonDataMap;
            }
            fixFlag = true;
            log.warn("verifyData the data: {} contains <script>, fix it to empty string", data);
        }

        if (data.contains(RtcConstants.FIX_VERTICAL_NUMBER)) {
            RtcUtil.replace(sbData, RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON);
            fixFlag = true;
            log.warn("verifyData the data: {} contains {} fix it to colon {}", data,
                    RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON);
        }

        if (data.contains(RtcConstants.FIX_LINE_FEED_CRLF)) {
            RtcUtil.replace(sbData, RtcConstants.FIX_LINE_FEED_CRLF, RtcConstants.EMPTY_STRING);
            fixFlag = true;
            log.warn("verifyData the data: {} contains line feed CRLF fix it to empty string",
                    data);
        }
        if (data.contains(RtcConstants.FIX_LINE_FEED_LF)) {
            RtcUtil.replace(sbData, RtcConstants.FIX_LINE_FEED_LF, RtcConstants.EMPTY_STRING);
            fixFlag = true;
            log.warn("verifyData the data: {} contains line feed LF fix it to empty string", data);
        }

        if (fixFlag)
            RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getFixNums().incrementAndGet();

        return Optional.ofNullable(RtcUtil.json2LinkedHashMap(sbData.toString()));
    }

    private String fixUserId(final Long recivedTime, String userId,
            LinkedHashMap<String, String> jsonDataMap) {

        String originDataUserId = jsonDataMap.get(RtcConstants.FIELD_USER_ID);
        if (originDataUserId == null) {

            log.error(
                    "fixUserId for the user_id: {} from jsonDataMap get user_id is null, return null.",
                    userId);
            return null;
        }

        boolean fixFlag = false;
        String dataUserId = originDataUserId.trim();
        if (!originDataUserId.equals(dataUserId)) {
            log.warn("fixUserId for user_id: {} the originDataUserId: {} trim to dataUserId: {}",
                    userId, originDataUserId, dataUserId);
            fixFlag = true;
        }

        if ((RtcConstants.EMPTY_STRING.equals(userId) && userId.equals(dataUserId))
                || (userId == null && RtcConstants.EMPTY_STRING.equals(dataUserId))) {
            log.warn("fixUserId for the user_id: {} and the dataUserId: {} is null, set to 0",
                    userId, dataUserId);
            fixFlag = true;
            dataUserId = RtcConstants.ZERO_STRING;
        }

        if (userId != null && !userId.equals(dataUserId) && !userId.equals(RtcConstants.ZERO_STRING)
                && dataUserId.equals(RtcConstants.ZERO_STRING)) {
            log.warn("fixUserId for the user_id: {} dont't equal dataUserId: {}", userId,
                    dataUserId);
            fixFlag = true;
            dataUserId = userId;
        }

        if (fixFlag) {
            RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getFixNums().incrementAndGet();
            jsonDataMap.put(RtcConstants.FIELD_USER_ID, dataUserId);
        }

        log.debug("fixUserId for user_id: {} get the dataUserid: {}", userId, dataUserId);
        return dataUserId;
    }

    private void fixHttpsqsTime(final Long recivedTime, LinkedHashMap<String, String> jsonDataMap) {

        Object objHttpSqsTime = jsonDataMap.get(RtcConstants.FIELD_HTTPSQS_TIME);
        String strRecivedTime = String.valueOf(recivedTime);

        if (objHttpSqsTime == null)
            jsonDataMap.put(RtcConstants.FIELD_HTTPSQS_TIME, strRecivedTime);
        else {
            log.warn("fixHttpsqsTime set dataHttpSqsTime from {} to {}", objHttpSqsTime,
                    strRecivedTime);
            jsonDataMap.put(RtcConstants.FIELD_HTTPSQS_TIME, strRecivedTime);
            RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getFixNums().incrementAndGet();
        }
    }

    private void fixFieldValue(final Long recivedTime, LinkedHashMap<String, String> jsonDataMap) {

        jsonDataMap.forEach((key, value) -> {

            if (!RtcConstants.EMPTY_STRING.equals(value)) {

                String okValue = value;
                if (okValue.contains(RtcConstants.FIX_VALUE_COMMA)) {
                    okValue = okValue.replaceAll(RtcConstants.FIX_VALUE_COMMA,
                            RtcConstants.EMPTY_STRING);
                    log.warn(
                            "fixFieldValue for the key: {} fix value: {} to {} because inclue comma",
                            key, value, okValue);
                }

                if (okValue.contains(RtcConstants.FIX_VALUE_STRING_FUNCTION_OBJECT)) {
                    log.warn(
                            "fixFieldValue for the key: {} fix value: {} to empty string because inclue: {}",
                            key, okValue, RtcConstants.FIX_VALUE_STRING_FUNCTION_OBJECT);
                    okValue = RtcConstants.EMPTY_STRING;
                }
                if (okValue.contains(RtcConstants.FIX_VALUE_STRING_NATIVE_CODE)) {
                    log.warn(
                            "fixFieldValue for the key: {} fix value: {} to empty string because inclue: {}",
                            key, okValue, RtcConstants.FIX_VALUE_STRING_NATIVE_CODE);
                    okValue = RtcConstants.EMPTY_STRING;
                }

                if (!value.equals(okValue)) {
                    jsonDataMap.put(key, okValue);
                    RtcCache.COLLECT_RESULT_MAP.get(recivedTime).getFixNums().incrementAndGet();
                }
            }
        });
    }
}
