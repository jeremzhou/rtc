/**
 * created on 2018年6月8日 上午9:41:50
 */
package cn.utstarcom.rtc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.utstarcom.rtc.common.RtcConstants;
import cn.utstarcom.rtc.util.RtcUtil;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
public final class JavaTest {

    private static final Logger logger = LoggerFactory.getLogger(JavaTest.class);

    public static void main(String[] args) throws URISyntaxException, IOException {

        logger.info("os: {}", System.getProperty("os.name").toLowerCase());

        final long curUnixTime = RtcUtil.getCurrentUnixTime();

        logger.info("{}", OffsetDateTime.now().getOffset());
        logger.info("localDateTime: {}",
                LocalDateTime.ofEpochSecond(curUnixTime, 0, OffsetDateTime.now().getOffset()));

        String st = "a|b|c|";
        logger.info("st replaceall: {}", st.replaceAll("\\|", ":"));

        logger.info("RtcConstants.EMPTY_STRING.equals(int): {} string: {}",
                RtcConstants.EMPTY_STRING.equals(9999), String.valueOf("9999a"));

        String uri = "/?opt=put&userid=07634036559&data=%7B\"action_type\":\"browsing\",\"user_id\":\"07634036559\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"terminal_type\":\"linux_STB\",\"page_id\":\"sd_mg_index.jsp\",\"page_name\":\"Ã¨ÂŠÂ’Ã¦ÂžÂœÃ¦Â Â‡Ã¦Â¸Â…Ã©Â¦Â–Ã©Â¡Âµ\",\"mediacode\":\"C01\",\"medianame\":\"biz_89401423\",\"log_time\":\"1532139633\",\"refer_type\":\"3\"%7D";

        String path = uri.split("\\?")[0];

        logger.info("path: {} match result: {}", path, uri.matches("^/NeHeartBeat$"));

        logger.info("match: {} result: {}", path, uri.matches(".*"));

        uri = "/NeHeartBeat";
        path = uri.split("\\?")[0];

        logger.info("path: {} match result: {}", path, uri.matches("^/NeHeartBeat$"));

        logger.info("match: {} result: {}", path, uri.matches(".*"));
    }

}
