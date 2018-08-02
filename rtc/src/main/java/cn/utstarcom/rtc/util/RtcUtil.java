/**
 * created on 2018年6月8日 下午2:10:15
 */
package cn.utstarcom.rtc.util;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.text.CharsetDetector;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
public final class RtcUtil {

    private static final Logger log = LoggerFactory.getLogger(RtcUtil.class);

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    private static final Type LINKED_HASHMAP_TYPE = new TypeToken<LinkedHashMap<String, String>>(){}.getType();

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private RtcUtil() {
        // do nothing.
    }

    public static final long getCurrentUnixTime() {

        return Instant.now().getEpochSecond();
    }

    public static final LocalDateTime unixTime2LocalDateTime(long unixTime) {

        return LocalDateTime.ofEpochSecond(unixTime, 0, OffsetDateTime.now().getOffset());
    }

    public static final String tryEncoding(String str) {

        return new CharsetDetector().setText(str.getBytes()).detect().getName();
    }

    public static final StringBuilder replace(final StringBuilder inSb, String oldPattern,
            String newPattern) {

        if (!(inSb.length() > 0) || !hasLength(oldPattern) || newPattern == null) {
            return inSb;
        }

        int index = inSb.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inSb;
        }

        int patLen = oldPattern.length();
        while (index >= 0) {
            inSb.replace(index, index + patLen, newPattern);
            index = inSb.indexOf(oldPattern);
        }

        return inSb;
    }

    public static final StringBuilder replace(final StringBuilder inSb, String startPattern,
            String endPattern, String newPattern) {

        if (!(inSb.length() > 0) || !hasLength(startPattern) || !hasLength(endPattern)
                || newPattern == null || endPattern == null) {
            return inSb;
        }

        if (startPattern.equals(endPattern)) {
            log.error("replace for the startPattern: {} equal endPattern: {} so return null.",
                    startPattern, endPattern);
            return null;
        }

        int startIndex = inSb.indexOf(startPattern);
        if (startIndex == -1) {
            // no occurrence -> can return input as-is
            return inSb;
        }

        int endIndex = inSb.indexOf(endPattern);
        if (endIndex == -1) {
            // no occurrence -> can return input as-is
            return inSb;
        }

        final int startPatLen = startPattern.length();
        final int endPatLen = endPattern.length();
        if (startIndex + startPatLen > endIndex + endPatLen) {
            log.error(
                    "replace for the inSb: {} startPattern: {} endPattern: {} (startIndex + startPatLen) {} greate than (endIndex + endPatLen) {} so return null.",
                    inSb, startPattern, endPattern, startIndex + startPatLen, endIndex + endPatLen);
            return null;
        }
        while (startIndex >= 0 && endIndex >= 0) {
            inSb.replace(startIndex, endIndex + endPatLen, newPattern);
            startIndex = inSb.indexOf(startPattern);
            endIndex = inSb.indexOf(endPattern);
            if (startIndex + startPatLen > endIndex + endPatLen) {
                log.error(
                        "replace for the inSb: {} startPattern: {} endPattern: {} (startIndex + startPatLen) {} greate than (endIndex + endPatLen) {} so return null.",
                        inSb, startPattern, endPattern, startIndex + startPatLen,
                        endIndex + endPatLen);
                return null;
            }
        }

        return inSb;
    }

    public static final boolean hasLength(String str) {

        return (str != null && !str.isEmpty());
    }

    public static final LinkedHashMap<String, String> json2LinkedHashMap(String json) {

        LinkedHashMap<String, String> jsonMap = null;
        try {
            jsonMap = GSON.fromJson(json, LINKED_HASHMAP_TYPE);
        } catch (JsonSyntaxException e) {
            log.error("json2LinkedHashMap for json: {} generate JsonSyntaxException:", json, e);
        }
        return jsonMap;
    }

    public static final String linkedHashMap2Json(LinkedHashMap<String, String> jsonMap) {

        String json = null;
        try {
            json = GSON.toJson(jsonMap);
        } catch (JsonIOException e) {
            log.error("linkedHashMap2Json for jsonMap: {} generate JsonIOException:", jsonMap, e);
        }
        return json;
    }
}
